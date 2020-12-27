package wiring;

import adapters.incoming.webserver.servlets.DataProvider;
import adapters.outgoing.databaseservice.AggregateDataProviderRepository;
import adapters.outgoing.databaseservice.EventDataProviderRepository;

// Used so it can use a stub instead of actually database
// TODO better name
public interface DataRespositoryFactoryInterface {
  DataProvider characterDataProvider();

  AggregateDataProviderRepository aggregateDataProviderRepository();

  EventDataProviderRepository eventDataProviderRepository();
}
