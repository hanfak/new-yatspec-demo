package acceptancetests._02databasestubpriming.givens;

import acceptancetests._02databasestubpriming.testinfrastructure.TestDataProvider;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.CharacterInfoRecord;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.SpeciesInfoId;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.SpeciesInfoRecord;
import com.googlecode.yatspec.state.givenwhenthen.TestState;

import static java.lang.String.format;

public class GivenTheDatabaseContains {

  private final TestState testState;
  private final TestDataProvider dataProvider;

  private CharacterInfoRecord characterInfoRecord;
  private SpeciesInfoRecord speciesInfoRecord;

  public GivenTheDatabaseContains(TestState testState, TestDataProvider dataProvider) {
    this.testState = testState;
    this.dataProvider = dataProvider;
  }

  public GivenTheDatabaseContains aCharacterStored(Integer personId, String name) {
    dataProvider.addCharacter(personId,name);
    return this;
  }

  public GivenTheDatabaseContains hasASpeciesInfo(SpeciesInfoRecord.SpeciesInfoRecordBuilder builder) {
    this.speciesInfoRecord = builder.build();
    return this;
  }

  public GivenTheDatabaseContains hasCharacterInfo(CharacterInfoRecord.CharacterInfoRecordBuilder characterInfoRecordBuilder) {
    this.characterInfoRecord = characterInfoRecordBuilder.build();
    return this;
  }

  public void isStoredInTheDatabase() {
    // For readability
    // Can also do the database insertions here
    primeSpeciesInfoTableInDatabase();
    primeCharacterInfoTableInDatabase();
  }

  private void primeCharacterInfoTableInDatabase() {
    int characterInfoId = dataProvider.addCharacterInfo(characterInfoRecord.getPersonId(), characterInfoRecord.getPersonName(), characterInfoRecord.getBirthYear());
    testState.interestingGivens().add("characterInfoId", characterInfoId);
    CharacterInfoRecord characterInfo = dataProvider.readCharacterInfoFromDatabase();
    testState.log(format("Character Info primed in database for characterInfoId '%s'", characterInfo.getCharacterInfoId()), characterInfo);
  }

  private void primeSpeciesInfoTableInDatabase() {
    int expectedSpeciesInfoId = dataProvider.addSpeciesInfo(speciesInfoRecord.getPersonId(), speciesInfoRecord.getName(), speciesInfoRecord.getAverageHeight(), speciesInfoRecord.getLifespan());
    testState.interestingGivens().add(new SpeciesInfoId(expectedSpeciesInfoId));
    SpeciesInfoRecord speciesInfoRecord = dataProvider.readSpeciesInfoFromDatabase();
    testState.log(format("Species Info primed in database for speciesInfo id '%s'", speciesInfoRecord.getSpeciesInfoId()), speciesInfoRecord);
  }
}
