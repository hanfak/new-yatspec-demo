package acceptancetests._02databasestubpriming.thens;

import acceptancetests._02databasestubpriming.testinfrastructure.TestDataProvider;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.CharacterInfoRecord;
import com.googlecode.yatspec.state.givenwhenthen.TestState;

import static org.assertj.core.api.Assertions.assertThat;

// Creating a builder
public class ThenTheCharacterInfoDatabaseContains {

  private CharacterInfoRecord expectedCharacterInfoId;

  private final TestState testState;
  private final TestDataProvider dataProvider;

  public ThenTheCharacterInfoDatabaseContains(TestState testState, TestDataProvider dataProvider) {
    this.testState = testState;
    this.dataProvider = dataProvider;
  }

  public ThenTheCharacterInfoDatabaseContains aCharacterInfo(CharacterInfoRecord.CharacterInfoRecordBuilder expectedCharacterInfoId) {
    this.expectedCharacterInfoId = expectedCharacterInfoId.build();
    return this;
  }

  public void wasInsertedIntoTheDatabase() {
    CharacterInfoRecord actualSpeciesInfoRecord = readCharacterInfoFromDatabase();
    assertThat(actualSpeciesInfoRecord).isEqualTo(expectedCharacterInfoId);
  }

  private CharacterInfoRecord readCharacterInfoFromDatabase() {
    CharacterInfoRecord characterInfoRecord = dataProvider.readCharacterInfoFromDatabase();
    testState.log(String.format("Character Info after test execution for characterInfoid '%s'", characterInfoRecord.getCharacterInfoId()), characterInfoRecord);
    return characterInfoRecord;
  }
}
