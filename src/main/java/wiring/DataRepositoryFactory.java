package wiring;

import adapters.incoming.webserver.servlets.DataProvider;
import adapters.outgoing.databaseservice.AggregateDataProviderRepository;
import adapters.outgoing.databaseservice.CharacterDataProvider;
import adapters.outgoing.databaseservice.EventDataProviderRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;

import javax.sql.DataSource;

public class DataRepositoryFactory implements DataRespositoryFactoryInterface {

  private final Singletons singletons;
  private final Logger logger;

  private static class Singletons {

    private final DSLContext dslContext;

    public Singletons(DSLContext dslContext) {
      this.dslContext = dslContext;
    }
  }

  public DataRepositoryFactory() {
    throw new AssertionError("Should not be instantiated outside of static factory method");
  }

  public DataRepositoryFactory(DataSource dataSource, Logger logger) {
    this.singletons = new Singletons(DSL.using(dataSource, SQLDialect.POSTGRES)); //todo necessary??
    this.logger = logger;
  }

  @Override
  public DataProvider characterDataProvider() {
    return new CharacterDataProvider(singletons.dslContext);
  }

  @Override
  public AggregateDataProviderRepository aggregateDataProviderRepository() {
    return new AggregateDataProviderRepository(singletons.dslContext, logger);
  }

  @Override
  public EventDataProviderRepository eventDataProviderRepository() {
    return new EventDataProviderRepository(singletons.dslContext, logger);
  }
}
