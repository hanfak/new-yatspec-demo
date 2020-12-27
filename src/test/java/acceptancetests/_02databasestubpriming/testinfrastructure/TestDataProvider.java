package acceptancetests._02databasestubpriming.testinfrastructure;

import acceptancetests._02databasestubpriming.testinfrastructure.stub.CharacterInfoRecord;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.CharacterRecord;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.SpeciesInfoId;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.SpeciesInfoRecord;
import adapters.logging.LoggingCategory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class TestDataProvider {

  static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  private static final AtomicInteger characterDatabaseId = new AtomicInteger(1);
  private static final AtomicInteger speciesInfoDatabaseId = new AtomicInteger(1);

  private final Map<Integer, CharacterRecord> characterDatabase;
  private final Map<Integer, CharacterInfoRecord> characterInfoDatabase;
  private final Map<Integer, SpeciesInfoRecord> speciesInfoDatabase;
  private final AtomicInteger characterInfoDatabaseId;

  public TestDataProvider(Map<Integer, CharacterRecord> characterDatabase, Map<Integer, CharacterInfoRecord> characterInfoDatabase, Map<Integer, SpeciesInfoRecord> speciesInfoDatabase, AtomicInteger characterInfoDatabaseId) {
    this.characterDatabase = characterDatabase;
    this.characterInfoDatabase = characterInfoDatabase;
    this.speciesInfoDatabase = speciesInfoDatabase;
    this.characterInfoDatabaseId = characterInfoDatabaseId;
  }

  public void deleteAllInfoFromAllTables() {
    APPLICATION_LOGGER.info("Deleting database before test");
    speciesInfoDatabase.clear();
    characterInfoDatabase.clear();
    characterDatabase.clear();
    characterDatabaseId.set(1);
    characterInfoDatabaseId.set(1);
    speciesInfoDatabaseId.set(1);
    APPLICATION_LOGGER.info("Database clean before test execution");
  }

  public void addCharacter(Integer personId, String name) {
    int id = characterDatabaseId.getAndIncrement();
    CharacterRecord value = new CharacterRecord(id, personId, name);
    characterDatabase.put(id, value);
  }

  public int addCharacterInfo(Integer personId, String personName, String birthYear) {
    int id = characterInfoDatabaseId.getAndIncrement();
    CharacterInfoRecord value = new CharacterInfoRecord(id, id, personId, birthYear, personName);
    characterInfoDatabase.put(id, value);
    return id;
  }

  public Integer addSpeciesInfo(Integer personId, String name, Float averageHeight, Integer lifespan) {
    int id = speciesInfoDatabaseId.getAndIncrement();
    SpeciesInfoRecord value = new SpeciesInfoRecord(id, new SpeciesInfoId(id), personId, name, averageHeight, lifespan);
    speciesInfoDatabase.put(id, value);
    return id;
  }

  public SpeciesInfoRecord readSpeciesInfoFromDatabase() {
    return speciesInfoDatabase.values().stream().findFirst()
        .orElseThrow(IllegalStateException::new);
  }

  public CharacterInfoRecord readCharacterInfoFromDatabase() {
    return characterInfoDatabase.values().stream().findFirst()
        .orElseThrow(IllegalStateException::new);
  }
}
