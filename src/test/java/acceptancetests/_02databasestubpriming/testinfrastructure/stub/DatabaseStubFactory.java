package acceptancetests._02databasestubpriming.testinfrastructure.stub;

import adapters.incoming.webserver.servlets.DataProvider;
import adapters.outgoing.databaseservice.AggregateDataProviderRepository;
import adapters.outgoing.databaseservice.EventDataProviderRepository;
import wiring.DataRespositoryFactoryInterface;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseStubFactory implements DataRespositoryFactoryInterface {

  private final Map<Integer, CharacterRecord> characterDatabase;
  private final Map<Integer, CharacterInfoRecord> characterInfoDatabase;
  private final Map<Integer, SpeciesInfoRecord> speciesInfoDatabase;
  private final AtomicInteger characterInfoDatabaseId;

  public DatabaseStubFactory(Map<Integer, CharacterRecord> characterDatabase, Map<Integer, CharacterInfoRecord> characterInfoDatabase, Map<Integer, SpeciesInfoRecord> speciesInfoDatabase, AtomicInteger characterInfoDatabaseId) {
    this.characterDatabase = characterDatabase;
    this.characterInfoDatabase = characterInfoDatabase;
    this.speciesInfoDatabase = speciesInfoDatabase;
    this.characterInfoDatabaseId = characterInfoDatabaseId;
  }

  @Override
  public DataProvider characterDataProvider() {
    return new CharacterDataProviderStub(characterDatabase,characterInfoDatabase, speciesInfoDatabase, characterInfoDatabaseId);
  }

  @Override
  public AggregateDataProviderRepository aggregateDataProviderRepository() {
    return null; // not needed for this test
  }

  @Override
  public EventDataProviderRepository eventDataProviderRepository() {
    return null; // not needed for this test
  }
}
