package acceptancetests._02databasestubpriming.thens;

import acceptancetests._02databasestubpriming.testinfrastructure.stub.SpeciesInfoRecord;
import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// Creating a builder
public class ThenTheDatabaseContains {

  private SpeciesInfoRecord expectedSpeciesInfo;

  private final TestState testState;
  private final Map<Integer, SpeciesInfoRecord> speciesInfoDatabase;

  public ThenTheDatabaseContains(TestState testState, Map<Integer, SpeciesInfoRecord> speciesInfoDatabase) {
    this.testState = testState;
    this.speciesInfoDatabase = speciesInfoDatabase;
  }

  public ThenTheDatabaseContains withSpeciesInfo(SpeciesInfoRecord.SpeciesInfoRecordBuilder expectedSpeciesInfoBuilder) {
    // We can also add default build methods here, and let the test override them
    this.expectedSpeciesInfo = expectedSpeciesInfoBuilder.build();
    return this;
  }

  // Using the expectedSpeciesInfo object to assert on instead of the individual fields
  public void wasInsertedIntoTheDatabase() {
    // Using this means that equals() needs to be overriden
    SpeciesInfoRecord actualSpeciesInfoRecord = readSpeciesInfoFromDatabase();
    assertThat(actualSpeciesInfoRecord).isEqualTo(expectedSpeciesInfo);
  }


  private SpeciesInfoRecord readSpeciesInfoFromDatabase() {
    SpeciesInfoRecord speciesInfoRecord = speciesInfoDatabase.values().stream().findFirst()
        .orElseThrow(IllegalStateException::new);
    // To show in html, the database state after the when is called
    //   Not useful here as no update/deletion/insertion occurring, but for an example
    testState.log(String.format("Species Info after test execution for speciesInfo id '%s'", speciesInfoRecord.getSpeciesInfoId()), speciesInfoRecord);
    return speciesInfoRecord;
  }
}
