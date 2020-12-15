package adapters.outgoing.databaseservice;

import core.usecases.ports.outgoing.AggregateDataProvider;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record5;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.jooq.sources.Tables.AGGREGATES;

public class AggregateDataProviderRepository implements AggregateDataProvider {

  private final DSLContext dslContext;
  private final Logger logger;

  public AggregateDataProviderRepository(DSLContext dslContext, Logger logger) {
    this.dslContext = dslContext;
    this.logger = logger;
  }

  @Override
  public void createAggregate(String aggregateJobReference, String aggregateState, String aggregateData) {
    logger.info("Creating aggregate");
    dslContext.insertInto(AGGREGATES)
        .set(AGGREGATES.AGG_REFERENCE, aggregateJobReference)
        .set(AGGREGATES.AGG_DATA, aggregateData)
        .set(AGGREGATES.AGG_STATE, aggregateState)
        .execute();
    logger.info("Finished creating aggregate");
  }

  @Override
  public void updateAggregateState(String aggregateJobReference, String aggregateState) {
    logger.info("Updating aggregate state");
    dslContext.update(AGGREGATES)
        .set(AGGREGATES.AGG_STATE, aggregateState)
        .set(AGGREGATES.LAST_MODIFIED_TIME, LocalDateTime.now())
        .where(AGGREGATES.AGG_REFERENCE.eq(aggregateJobReference))
        .execute();
    logger.info("Finished updating aggregate state");
  }

  @Override
  public Optional<String> findAggregateData(String aggregateJobReference) {
    return dslContext.select(AGGREGATES.AGG_DATA)
        .from(AGGREGATES)
        .where(AGGREGATES.AGG_REFERENCE.eq(aggregateJobReference))
        .fetchOptional()
        .map(Record1::component1);
  }

  // TODO to remove, use in test impl
  public String findAggregate(String aggregateJobReference) {
    // TODO not correct but just for intermediate testing, will be used in tests
    Optional<Record5<Integer, String, String, LocalDateTime, LocalDateTime>> result = dslContext.select(AGGREGATES.AGG_ID, AGGREGATES.AGG_REFERENCE, AGGREGATES.AGG_STATE, AGGREGATES.LAST_MODIFIED_TIME, AGGREGATES.CREATED_AT)
        .from(AGGREGATES)
        .where(AGGREGATES.AGG_REFERENCE.eq(aggregateJobReference))
        .fetchOptional();
    String s = result.map(x -> x.component1() + " " + x.component2() + " " + x.component3() + " " + x.component4() + " " + x.component5()).orElse("afsf");
    logger.info(s);
    return "";
  }
}
