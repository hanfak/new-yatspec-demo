package adapters.outgoing.fileservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import core.domain.FailedEvent;
import core.domain.SuccessfullyProcessedEvent;

import java.util.List;

public class AggregateCompletionDetailsJacksonDTO {

  private final String aggregateReference;
  private final String aggregateState;
  private final long numberOfProcessedEvents;
  private final List<SuccessfullyProcessedEvent> processedEvents;
  private final long numberOfInErrorEvents;
  private final List<FailedEvent> failedEvents;

  //TODO wrap processed and fail events in objects
  @JsonCreator
  @JsonPropertyOrder({ "aggregateReference", "aggregateState", "numberOfProcessedEvents", "processedEvents", "numberOfInErrorEvents", "failedEvents"})
  public AggregateCompletionDetailsJacksonDTO(
      @JsonProperty(value = "aggregateReference", required = true) String aggregateReference,
      @JsonProperty(value = "aggregateState", required = true) String aggregateState,
      @JsonProperty(value = "numberOfProcessedEvents", required = true) long numberOfProcessedEvents,
      @JsonProperty(value = "processedEvents", required = true) List<SuccessfullyProcessedEvent> processedEvents,
      @JsonProperty(value = "numberOfInErrorEvents", required = true) long numberOfInErrorEvents,
      @JsonProperty(value = "failedEvents", required = true) List<FailedEvent> failedEvents) {
    this.aggregateReference = aggregateReference;
    this.aggregateState = aggregateState;
    this.numberOfProcessedEvents = numberOfProcessedEvents;
    this.processedEvents = processedEvents;
    this.numberOfInErrorEvents = numberOfInErrorEvents;
    this.failedEvents = failedEvents;
  }

  public String getAggregateReference() {
    return aggregateReference;
  }

  public String getAggregateState() {
    return aggregateState;
  }

  public long getNumberOfProcessedEvents() {
    return numberOfProcessedEvents;
  }

  public List<SuccessfullyProcessedEvent> getProcessedEvents() {
    return processedEvents;
  }

  public long getNumberOfInErrorEvents() {
    return numberOfInErrorEvents;
  }

  public List<FailedEvent> getFailedEvents() {
    return failedEvents;
  }
}
