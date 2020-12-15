package core.usecases.services.aggregateexample.exampleone;

import core.domain.EventState;
import core.usecases.ports.incoming.AggregateExample1Step2Service;
import core.usecases.ports.outgoing.AggregateDataProvider;
import core.usecases.ports.outgoing.EventDataProvider;
import core.usecases.ports.outgoing.InstructionFactory;
import core.usecases.ports.outgoing.MessageService;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;

import static core.domain.AggregateState.PROCESSING_AGGREGATE;
import static core.domain.EventState.CREATED;
import static core.domain.EventState.IN_ERROR;
import static java.util.Arrays.stream;
import static java.util.function.Predicate.not;

// TODO: capture error, use decorator, update aggregate table with error
public class UseCaseAggregateExample1Step2 implements AggregateExample1Step2Service {

  private static final String LOCATION = "EventQueueOne";

  private final AggregateDataProvider aggregateDataProvider;
  private final EventDataProvider eventDataProvider;
  private final MessageService messageService;
  private final InstructionFactory instructionFactory;
  private final Logger logger;

  public UseCaseAggregateExample1Step2(AggregateDataProvider aggregateDataProvider, EventDataProvider eventDataProvider, MessageService messageService, InstructionFactory instructionFactory, Logger logger) {
    this.aggregateDataProvider = aggregateDataProvider;
    this.eventDataProvider = eventDataProvider;
    this.messageService = messageService;
    this.instructionFactory = instructionFactory;
    this.logger = logger;
  }

  //run when get message from queue
  @Override
  public void execute(AggregateExample1Step2IncomingInstruction instruction) {
    aggregateDataProvider.updateAggregateState(instruction.getAggregateReference(), PROCESSING_AGGREGATE.name());

    Optional<String> aggregateData = aggregateDataProvider.findAggregateData(instruction.getAggregateReference());
    // Might do some business rules to the data in aggregate
    aggregateData.map(data -> stream(data.split(" ")))
        .orElseThrow(() -> new IllegalStateException("No data")) // case where no data exists, TODO update to have error in aggregate, or send on message, and step 4 needs to match on this ow message is continously resent
        .map(individualData -> {
          Integer event = eventDataProvider.createEvent(instruction.getAggregateReference(), CREATED.name(), individualData);
          return new EventInformation(event, individualData, CREATED.name());
        })
        .map(this::updateStateWithErrorForInvalidData)
        .filter(not(eventInformation -> Objects.equals(eventInformation.getEventState(), "IN_ERROR")))
        .forEach(eventInformation -> {
          messageService.send(LOCATION, instructionFactory.createEventQueueOneInstruction(eventInformation.getEventId()));
          eventDataProvider.updateEventState(eventInformation.getEventId(), EventState.SENT_FOR_PROCESSING.name());
        });
  }

  // this can be done in UseCaseAggregateExample1Step3 but done here to reduce the number of messages sent with invalid data
  private EventInformation updateStateWithErrorForInvalidData(EventInformation eventInformation) {
    if (eventInformation.getEventData().isBlank()) {
      eventDataProvider.updateEventWithError(eventInformation.getEventId(),"EMPTY_DATA", "Data was empty", IN_ERROR.name());
      return new EventInformation( eventInformation.getEventId(), eventInformation.getEventData(), IN_ERROR.name());
    } else {
      return eventInformation;
    }
  }

  // Temp object to allow stream to continue
  private static class EventInformation {
    private final Integer eventId;
    private final String eventData;
    private final String eventState;

    private EventInformation(Integer eventId, String eventData, String eventState) {
      this.eventId = eventId;
      this.eventData = eventData;
      this.eventState = eventState;
    }

    public Integer getEventId() {
      return eventId;
    }

    public String getEventData() {
      return eventData;
    }

    public String getEventState() {
      return eventState;
    }
  }


  // Why was this split out from UseCaseAggregateExample1Step1?
  //    Here we might need to limit the number of consumers (ie only allowed one user to access service),
  //    where the previous step might allow multiple consumers
}
