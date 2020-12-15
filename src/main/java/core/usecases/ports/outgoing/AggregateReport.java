package core.usecases.ports.outgoing;

import core.domain.FailedEvent;
import core.domain.SuccessfullyProcessedEvent;

import java.util.List;

public interface AggregateReport {

  void generate(AggregateCompletionDetails details);

  class AggregateCompletionDetails {

    private final String aggregateReference;
    private final String aggregateState;
    private final long processedEvents;
    private final List<SuccessfullyProcessedEvent> successfulEvents;
    private final long numberOfInErrorEvents;
    private final List<FailedEvent> failedEvents;

    public AggregateCompletionDetails(String aggregateReference, String aggregateState, long processedEvents, List<SuccessfullyProcessedEvent> successfulEvents, long numberOfInErrorEvents, List<FailedEvent> failedEvents) {
      this.aggregateReference = aggregateReference;
      this.aggregateState = aggregateState;
      this.processedEvents = processedEvents;
      this.successfulEvents = successfulEvents;
      this.numberOfInErrorEvents = numberOfInErrorEvents;
      this.failedEvents = failedEvents;
    }

    public String getAggregateReference() {
      return aggregateReference;
    }

    public String getAggregateState() {
      return aggregateState;
    }

    public long getProcessedEvents() {
      return processedEvents;
    }

    public List<SuccessfullyProcessedEvent> getSuccessfulEvents() {
      return successfulEvents;
    }

    public long getNumberOfInErrorEvents() {
      return numberOfInErrorEvents;
    }

    public List<FailedEvent> getFailedEvents() {
      return failedEvents;
    }
  }

}
