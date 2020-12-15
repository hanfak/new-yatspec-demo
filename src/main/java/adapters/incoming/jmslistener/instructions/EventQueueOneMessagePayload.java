package adapters.incoming.jmslistener.instructions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventQueueOneMessagePayload {
    private final Integer eventId;

    @JsonCreator
    public EventQueueOneMessagePayload(
        @JsonProperty(value = "eventId", required = true) Integer eventId) {
      this.eventId = eventId;
    }

  public Integer getEventId() {
    return eventId;
  }
}

