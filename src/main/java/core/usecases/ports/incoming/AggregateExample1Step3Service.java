package core.usecases.ports.incoming;

import javax.validation.constraints.NotNull;

public interface AggregateExample1Step3Service {

  void execute(EventIncomingInstruction instruction);

  final class EventIncomingInstruction {

    @NotNull private final int eventId;

    private EventIncomingInstruction(int eventId) {
      this.eventId = eventId;
    }

    public static EventIncomingInstruction incomingInstruction(int eventId) {
      return new EventIncomingInstruction(eventId);
    }

    public int getEventId() {
      return eventId;
    }
  }
}
