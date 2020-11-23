package acceptancetests._02databasepriming.givens;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;

public class GivenTheDatabaseContainsVersion2 {

  private final DataSource dataSource;
  private final DSLContext dslContext;
  private final TestState testState;

  // tODO extract to separate class dslcontextadpater
  public GivenTheDatabaseContainsVersion2(DataSource dataSource, TestState testState) {
    this.dataSource = dataSource;
    this.testState = testState;
    dslContext = DSL.using(this.dataSource, SQLDialect.POSTGRES);
  }

  public GivenTheDatabaseContainsVersion2 aSpeciesInfo(SpeciesInfoRecord.SpeciesInfoRecordBuilder builder) {
    SpeciesInfoRecord speciesInfoRecord = builder.build();
    // Can add important primings into a map, and display in output
    testState.interestingGivens().add("SpeciesInfoRecord specific key", speciesInfoRecord);
    // Can use ths instance, and have the name of the class as the key, good if using domain objects
    testState.interestingGivens().add(speciesInfoRecord);

    //Contrived example, as can get state from variable on line 27
    // Can grab the data from entry in interesting givens using the class, instead of using key in line 29
    SpeciesInfoRecord speciesInfoRecordFromInterestingGivens = testState.interestingGivens().getType(SpeciesInfoRecord.class);
    dslContext.insertInto(SPECIFIESINFO)
        .set(SPECIFIESINFO.PERSON_ID, speciesInfoRecordFromInterestingGivens.getId())
        .set(SPECIFIESINFO.SPECIES, speciesInfoRecordFromInterestingGivens.getName())
        .set(SPECIFIESINFO.AVG_HEIGHT, speciesInfoRecordFromInterestingGivens.getAverageHeight())
        .set(SPECIFIESINFO.LIFESPAN, speciesInfoRecordFromInterestingGivens.getLifespan())
        .execute();
    return this;
  }

  public GivenTheDatabaseContainsVersion2 aCharacterStored(Integer personId, String name) {
    dslContext.insertInto(CHARACTERS)
        .set(CHARACTERS.PERSON_ID, personId)
        .set(CHARACTERS.PERSON_NAME, name)
        .execute();
    return this;
  }

  public void isStoredInTheDatabase() {
    // For readability
    // Can also do the database insertions here
  }
}
