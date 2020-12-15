package core.domain;

public class SuccessfullyProcessedEvent {

  private final int eventId;
  private final String eventData;
  private final String eventState;

  public SuccessfullyProcessedEvent(int eventId, String eventData, String eventState) {
    this.eventId = eventId;
    this.eventData = eventData;
    this.eventState = eventState;
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

  public static class SuccessfullyProcessedEventBuilder {

    private int eventId;
    private String eventData;
    private String eventState;

    private SuccessfullyProcessedEventBuilder() {
    }

    public static SuccessfullyProcessedEventBuilder successfullyProcessedEventBuilder() {
      return new SuccessfullyProcessedEventBuilder();
    }

    public SuccessfullyProcessedEventBuilder withEventId(int eventId) {
      this.eventId = eventId;
      return this;
    }

    public SuccessfullyProcessedEventBuilder withEventData(String eventData) {
      this.eventData = eventData;
      return this;
    }

    public SuccessfullyProcessedEventBuilder withEventState(String eventState) {
      this.eventState = eventState;
      return this;
    }

    public SuccessfullyProcessedEvent build() {
      return new SuccessfullyProcessedEvent(eventId, eventData, eventState);
    }
  }
}
