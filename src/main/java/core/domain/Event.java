package core.domain;

public class Event {

  private final int eventId;
  private final String aggregateReference;
  private final String eventData;
  private final String eventState;
  private final String errorCode;
  private final String errorMessage;

  public Event(String aggregateReference, int eventId, String eventData, String eventState, String errorCode, String errorMessage) {
    this.aggregateReference = aggregateReference;
    this.eventId = eventId;
    this.eventData = eventData;
    this.eventState = eventState;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public int getEventId() {
    return eventId;
  }

  public String getAggregateReference() {
    return aggregateReference;
  }

  public String getEventData() {
    return eventData;
  }

  public String getEventState() {
    return eventState;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
