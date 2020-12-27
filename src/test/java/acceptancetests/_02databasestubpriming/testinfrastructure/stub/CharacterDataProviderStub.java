package acceptancetests._02databasestubpriming.testinfrastructure.stub;

import adapters.incoming.webserver.servlets.DataProvider;
import core.domain.Person;
import core.domain.SpeciesInfo;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CharacterDataProviderStub implements DataProvider {

  private final Map<Integer, CharacterRecord> characterDatabase;
  private final Map<Integer, CharacterInfoRecord> characterInfoDatabase;
  private final Map<Integer, SpeciesInfoRecord> speciesInfoDatabase;
  private final AtomicInteger characterInfoDatabaseId;

  public CharacterDataProviderStub(Map<Integer, CharacterRecord> characterDatabase, Map<Integer, CharacterInfoRecord> characterInfoDatabase, Map<Integer, SpeciesInfoRecord> speciesInfoDatabase, AtomicInteger characterInfoDatabaseId) {
    this.characterDatabase = characterDatabase;
    this.characterInfoDatabase = characterInfoDatabase;
    this.speciesInfoDatabase = speciesInfoDatabase;
    this.characterInfoDatabaseId = characterInfoDatabaseId;
  }

  @Override
  public Integer getPersonId(String personName) {
    return characterDatabase.values().stream()
        .filter(characterRecord -> characterRecord.getPersonName().equals(personName))
        .findFirst()
        .map(CharacterRecord::getPersonId)
        .orElse(0);
  }

  @Override
  public void storeCharacterInfo(String personId, Person characterInfo) {
    int id = characterInfoDatabaseId.getAndIncrement();
    CharacterInfoRecord value = new CharacterInfoRecord(id, id, Integer.parseInt(personId), characterInfo.getName(), characterInfo.getBirthYear());
    characterInfoDatabase.put(id, value);  }

  @Override
  public SpeciesInfo getSpeciesInfo(Integer personId) {
    SpeciesInfoRecord speciesInfoRecord = speciesInfoDatabase.values().stream()
        .filter(speciesInfoRecord1 -> speciesInfoRecord1.getPersonId().equals(personId))
        .findFirst()
        .orElseThrow(IllegalStateException::new);
    return new SpeciesInfo(speciesInfoRecord.getName(), speciesInfoRecord.getLifespan(), speciesInfoRecord.getAverageHeight());
  }

  @Override
  public String getBirthYear(int personId) {
    return characterInfoDatabase.values().stream()
        .filter(characterInfoRecord -> characterInfoRecord.getPersonId().equals(personId))
        .map(CharacterInfoRecord::getBirthYear)
        .findFirst()
        .orElseThrow(IllegalStateException::new);
  }
}
