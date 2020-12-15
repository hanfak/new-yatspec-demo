package core.usecases.services.aggregateexample.exampleone;

import core.usecases.ports.incoming.AggregateExample1Step3Service;
import core.usecases.ports.outgoing.EventDataProvider;

import java.util.Objects;
import java.util.Optional;

import static core.domain.EventState.IN_ERROR;
import static core.domain.EventState.PROCESSED;
import static core.domain.EventState.PROCESSING_EVENT;

// TODO: capture error, use decorator, update event table with error
public class UseCaseAggregateExample1Step3 implements AggregateExample1Step3Service {

  private final EventDataProvider eventDataProvider;

  public UseCaseAggregateExample1Step3(EventDataProvider eventDataProvider) {
    this.eventDataProvider = eventDataProvider;
  }

  //run when get message from queue
  @Override
  public void execute(EventIncomingInstruction instruction) {
    eventDataProvider.updateEventState(instruction.getEventId(), PROCESSING_EVENT.name());

    // Do something with event data, apply business rules, call other services  etc
    Optional<String> eventData = eventDataProvider.findEventData(instruction.getEventId());
    // makes processing random, so logs can have multiple checks on whether job is comeplete
    // try {
    //   Thread.sleep(new Random().nextInt(10000) + 10000) ;
    // } catch (InterruptedException e) {
    //   throw new IllegalStateException(e);
    // }
    eventData.ifPresent(data -> {
      if (Objects.equals("blah",data)) { // Or whatever error condition
        eventDataProvider.updateEventWithError(instruction.getEventId(), "INVALID_DATA", createErrorMessage(instruction.getEventId(), data), IN_ERROR.name());
        return;
      }

      String newData = applyBusinessRules(data);

      eventDataProvider.updateEventData(instruction.getEventId(), newData);
      eventDataProvider.updateEventState(instruction.getEventId(), PROCESSED.name()); // this could be combined with line 33 in the database, for clarity it is separate step, if this affects performance then change
    });

  }

  private String applyBusinessRules(String eventData) {
    return eventData.toUpperCase() + " " + eventData.length();
  }

  private String createErrorMessage(int eventReference, String eventData) {
    return String.format("Data for event %s was %s and did not meet business rules", eventReference, eventData);
  }
}
