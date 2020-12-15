package core.usecases.ports.outgoing;

import core.domain.Event;

import java.util.List;
import java.util.Optional;

public interface EventDataProvider {
  Integer createEvent(String aggregateJobReference, String eventState, String eventData);
  void updateEventData(int eventReference, String newEventData);
  void updateEventState(int eventReference, String newEventState);
  void updateEventWithError(int eventReference, String errorCode, String errorMessage, String newEventState);
  Optional<String> findEventData(int eventReference);
  List<Event> findAllEventsForAggregate(String aggregateJobReference);
}
