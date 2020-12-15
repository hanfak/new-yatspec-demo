package core.usecases.services.aggregateexample.exampleone;

import core.domain.AggregateState;
import core.domain.Event;
import core.domain.FailedEvent;
import core.domain.SuccessfullyProcessedEvent;
import core.usecases.ports.incoming.AggregateExample1Step4CompletedService;
import core.usecases.ports.outgoing.*;
import core.usecases.ports.outgoing.AggregateReport.AggregateCompletionDetails;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static core.domain.AggregateState.COMPLETED;
import static core.domain.AggregateState.COMPLETED_WITH_ERROR;
import static core.domain.EventState.IN_ERROR;
import static core.domain.EventState.PROCESSED;
import static core.domain.FailedEvent.FailedEventBuilder.failedEventBuilder;
import static core.domain.SuccessfullyProcessedEvent.SuccessfullyProcessedEventBuilder.successfullyProcessedEventBuilder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

public class UseCaseAggregateExample1Step4Completed implements AggregateExample1Step4CompletedService {

  private static final String LOCATION = "InternalJobQueue";

  private final AggregateDataProvider aggregateDataProvider;
  private final EventDataProvider eventDataProvider;
  private final MessageService messageService;
  private final InstructionFactory instructionFactory;
  private final AggregateReport aggregateReport;
  private final InternalJobSettings settings;
  private final Logger logger;

  public UseCaseAggregateExample1Step4Completed(AggregateDataProvider aggregateDataProvider, EventDataProvider eventDataProvider, MessageService messageService, InstructionFactory instructionFactory, AggregateReport aggregateReport, InternalJobSettings settings, Logger logger) {
    this.aggregateDataProvider = aggregateDataProvider;
    this.eventDataProvider = eventDataProvider;
    this.messageService = messageService;
    this.instructionFactory = instructionFactory;
    this.aggregateReport = aggregateReport;
    this.settings = settings;
    this.logger = logger;
  }

  // TODO: capture error, use decorator, requeue message
  //run when get message from queue
  // Run by the internal job queue cron job
  @Override
  public void execute(AggregateExample1Step4CompletedIncomingInstruction instruction) {
    List<Event> allEventsForAggregate = eventDataProvider.findAllEventsForAggregate(instruction.getAggregateReference());

    Predicate<Event> isProcessedEvent = event -> Objects.equals(event.getEventState(), PROCESSED.name());
    Predicate<Event> isInErrorEvent = event -> Objects.equals(event.getEventState(), IN_ERROR.name());
    boolean allEventsProcessed = !allEventsForAggregate.isEmpty() &&
        allEventsForAggregate.stream().allMatch(isProcessedEvent.or(isInErrorEvent));

    if (allEventsProcessed) {
      logger.info("finished");
      // find all in error
      List<Event> eventsInError = allEventsForAggregate.stream()
          .filter(isInErrorEvent)
          .collect(toList());
      // determine state of aggregate
      AggregateState aggregateState = eventsInError.isEmpty() ? COMPLETED : COMPLETED_WITH_ERROR;
      // create report
      generateReport(instruction, allEventsForAggregate, isProcessedEvent, aggregateState, isInErrorEvent);
      // update aggregate to completed
      aggregateDataProvider.updateAggregateState(instruction.getAggregateReference(), aggregateState.name());
    } else {
      logger.info("Still not finished");
      // put back on queue with cron
      resendMessage(instruction);
    }
  }

  private void resendMessage(AggregateExample1Step4CompletedIncomingInstruction instruction) {
    messageService.send(
        LOCATION,
        instructionFactory.createInternalQueueInstruction(instruction.getAggregateReference()),
        settings.getAggregateCompleteDelayDuration());
  }

  private void generateReport(AggregateExample1Step4CompletedIncomingInstruction instruction, List<Event> allEventsForAggregate, Predicate<Event> isProcessedEvent, AggregateState aggregateState, Predicate<Event> isInErrorEvent) {
    long processedEvents = processedEvents(allEventsForAggregate, isProcessedEvent);
    long numberOfInErrorEvents = allEventsForAggregate.size() - processedEvents;

    List<FailedEvent> failedEvents = failedEvents(allEventsForAggregate, isInErrorEvent);
    List<SuccessfullyProcessedEvent> successfulEvents = successfulEvents(allEventsForAggregate, isProcessedEvent);
    aggregateReport.generate(new AggregateCompletionDetails(
        instruction.getAggregateReference(), aggregateState.name(), processedEvents, successfulEvents, numberOfInErrorEvents, failedEvents));
  }

  private List<FailedEvent> failedEvents(List<Event> allEventsForAggregate, Predicate<Event> isInErrorEvent) {
    return allEventsForAggregate.stream()
        .filter(isInErrorEvent)
        .map(this::createFailedEvent)
        .collect(toUnmodifiableList());
  }

  private FailedEvent createFailedEvent(Event event) {
    return failedEventBuilder()
        .withEventId(event.getEventId())
        .withEventData(event.getEventData())
        .withEventState(event.getEventState())
        .withErrorCode(event.getErrorCode())
        .withErrorMessage(event.getErrorMessage())
        .build();
  }

  private List<SuccessfullyProcessedEvent> successfulEvents(List<Event> allEventsForAggregate, Predicate<Event> processedEvents) {
    return allEventsForAggregate.stream()
        .filter(processedEvents)
        .map(this::createSuccessfullyProcessedEvent)
        .collect(toUnmodifiableList());  }

  private SuccessfullyProcessedEvent createSuccessfullyProcessedEvent(Event event) {
    return successfullyProcessedEventBuilder()
        .withEventId(event.getEventId())
        .withEventData(event.getEventData())
        .withEventState(event.getEventState())
        .build();
  }

  private long processedEvents(List<Event> allEventsForAggregate, Predicate<Event> processedEvents) {
    return allEventsForAggregate.stream().filter(processedEvents).count();
  }
}
