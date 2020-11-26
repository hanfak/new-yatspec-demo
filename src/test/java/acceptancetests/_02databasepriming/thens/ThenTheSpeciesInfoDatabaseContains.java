package acceptancetests._02databasepriming.thens;

import acceptancetests._02databasepriming.givens.SpeciesInfoId;
import acceptancetests._02databasepriming.givens.SpeciesInfoRecord;
import acceptancetests._02databasepriming.testinfrastructure.TestDataProvider;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.jooq.DSLContext;
import org.jooq.Record5;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.sources.Tables.SPECIFIESINFO;

// Creating a builder
public class ThenTheSpeciesInfoDatabaseContains {

  private SpeciesInfoId expectedSpeciesInfoId;
  private Integer expectedPersonId;
  private String expectedName;
  private Float expectedAverageHeight;
  // If we did not want to assert on a specific field/col in table/object,
  // we can use optional, and thus avoid having lots of build() methods for
  // all the permutations of fields to assert on
  private Optional<Integer> expectedLifespan = Optional.empty();

  private SpeciesInfoRecord expectedSpeciesInfo;

  private final TestState testState;
  private final TestDataProvider dataProvider;

  public ThenTheSpeciesInfoDatabaseContains(TestState testState, TestDataProvider dataProvider) {
    this.testState = testState;
    this.dataProvider = dataProvider;
  }

  public ThenTheSpeciesInfoDatabaseContains withSpeciesInfo(SpeciesInfoRecord.SpeciesInfoRecordBuilder expectedSpeciesInfoBuilder) {
    this.expectedSpeciesInfo = expectedSpeciesInfoBuilder.build();
    return this;
  }

  public void wasInsertedIntoTheDatabase() {
    SpeciesInfoRecord actualSpeciesInfoRecord = dataProvider.readSpeciesInfoFromDatabase();
    testState.log(String.format("Species Info after test execution for speciesInfo id '%s'", actualSpeciesInfoRecord.getSpeciesInfoId()), actualSpeciesInfoRecord);
    assertThat(actualSpeciesInfoRecord).isEqualTo(expectedSpeciesInfo);
  }
}
