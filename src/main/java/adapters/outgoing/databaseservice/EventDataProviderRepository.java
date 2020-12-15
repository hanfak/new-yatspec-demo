package adapters.outgoing.databaseservice;

import core.domain.Event;
import core.usecases.ports.outgoing.EventDataProvider;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.jooq.sources.Tables.AGGREGATES;
import static org.jooq.sources.Tables.EVENTS;

public class EventDataProviderRepository implements EventDataProvider {

  private final DSLContext dslContext;
  private final Logger logger;

  public EventDataProviderRepository(DSLContext dslContext, Logger logger) {
    this.dslContext = dslContext;
    this.logger = logger;
  }

  @Override
  public Integer createEvent(String aggregateJobReference, String eventState, String eventData) {
    logger.info("Creating event");
    Integer eventId = dslContext.insertInto(EVENTS, EVENTS.AGG_REFERENCE, EVENTS.EVENT_DATA, EVENTS.EVENT_STATE)
        .values(aggregateJobReference, eventData, eventState)
        .returningResult(EVENTS.EVENT_ID)
        .fetchOne()
        .component1();
    logger.info("Finished creating event");
    return eventId;
  }

  @Override
  public void updateEventData(int eventId, String newEventData) {
    dslContext.update(EVENTS)
        .set(EVENTS.EVENT_DATA, newEventData)
        .set(AGGREGATES.LAST_MODIFIED_TIME, LocalDateTime.now())
        .where(EVENTS.EVENT_ID.eq(eventId))
        .execute();
  }

  @Override
  public void updateEventState(int eventId, String newEventState) {
    dslContext.update(EVENTS)
        .set(EVENTS.EVENT_STATE, newEventState)
        .set(AGGREGATES.LAST_MODIFIED_TIME, LocalDateTime.now())
        .where(EVENTS.EVENT_ID.eq(eventId))
        .execute();
  }

  @Override
  public void updateEventWithError(int eventId, String errorCode, String errorMessage, String newEventState) {
    dslContext.update(EVENTS)
        .set(EVENTS.ERROR_CODE, errorCode)
        .set(EVENTS.ERROR_MESSAGE, errorMessage)
        .set(EVENTS.EVENT_STATE, newEventState)
        .set(AGGREGATES.LAST_MODIFIED_TIME, LocalDateTime.now())
        .where(EVENTS.EVENT_ID.eq(eventId))
        .execute();
  }

  @Override
  public Optional<String> findEventData(int eventId) {
    return dslContext.select(EVENTS.EVENT_DATA)
        .from(EVENTS)
        .where(EVENTS.EVENT_ID.eq(eventId))
        .fetchOptional()
        .map(Record1::component1);
  }

  @Override
  public List<Event> findAllEventsForAggregate(String aggregateJobReference) {
    return Arrays.stream(dslContext.select(EVENTS.EVENT_ID, EVENTS.AGG_REFERENCE, EVENTS.EVENT_DATA, EVENTS.EVENT_STATE, EVENTS.ERROR_CODE, EVENTS.ERROR_MESSAGE)
        .from(EVENTS)
        .where(EVENTS.AGG_REFERENCE.eq(aggregateJobReference))
        .fetchArray())
        .map(record -> new Event(record.component2(), record.component1(), record.component3(), record.component4(), record.component5(), record.component6()))
        .collect(toList());
  }
}
