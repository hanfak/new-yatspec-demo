package core.domain;

public class FailedEvent {

  private final int eventId;
  private final String eventData;
  private final String eventState;
  private final String errorCode;
  private final String errorMessage;

  private FailedEvent(int eventId, String eventData, String eventState, String errorCode, String errorMessage) {
    this.eventId = eventId;
    this.eventData = eventData;
    this.eventState = eventState;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public int getEventId() {
    return eventId;
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

  public static class FailedEventBuilder {

    private int eventId;
    private String eventData;
    private String eventState;
    private String errorCode;
    private String errorMessage;

    private FailedEventBuilder() {
    }

    public static FailedEventBuilder failedEventBuilder() {
      return new FailedEventBuilder();
    }

    public FailedEventBuilder withEventId(int eventId) {
      this.eventId = eventId;
      return this;
    }

    public FailedEventBuilder withEventData(String eventData) {
      this.eventData = eventData;
      return this;
    }

    public FailedEventBuilder withEventState(String eventState) {
      this.eventState = eventState;
      return this;
    }

    public FailedEventBuilder withErrorCode(String errorCode) {
      this.errorCode = errorCode;
      return this;
    }

    public FailedEventBuilder withErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
      return this;
    }

    public FailedEvent build() {
      return new FailedEvent(eventId, eventData, eventState, errorCode, errorMessage);
    }
  }
}
