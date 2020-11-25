package acceptancetests._02databasepriming.givens;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.jooq.DSLContext;
import org.jooq.Record5;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.Optional;

import static java.lang.String.format;
import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;

public class GivenTheDatabaseContainsVersion4 {

  private final DSLContext dslContext;
  private final TestState testState;

  // tODO extract to separate class dslcontextadpater
  public GivenTheDatabaseContainsVersion4(DataSource dataSource, TestState testState) {
    this.testState = testState;
    dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
  }

  public GivenTheDatabaseContainsVersion4 aSpeciesInfo(SpeciesInfoRecord.SpeciesInfoRecordBuilder builder) {
    SpeciesInfoRecord speciesInfoRecord = builder.build();
    testState.interestingGivens().add(speciesInfoRecord);
    SpeciesInfoRecord speciesInfoRecordFromInterestingGivens = testState.interestingGivens().getType(SpeciesInfoRecord.class);
    int expectedSpeciesInfoId = dslContext.insertInto(SPECIFIESINFO)
        .set(SPECIFIESINFO.PERSON_ID, speciesInfoRecord.getPersonId())
        .set(SPECIFIESINFO.SPECIES, speciesInfoRecord.getName())
        .set(SPECIFIESINFO.AVG_HEIGHT, speciesInfoRecord.getAverageHeight())
        .set(SPECIFIESINFO.LIFESPAN, speciesInfoRecord.getLifespan())
        .execute();
    return this;
  }

  public GivenTheDatabaseContainsVersion4 aCharacterStored(Integer personId, String name) {
    dslContext.insertInto(CHARACTERS)
        .set(CHARACTERS.PERSON_ID, personId)
        .set(CHARACTERS.PERSON_NAME, name)
        .execute();
    return this;
  }

  public void isStoredInTheDatabase() {
    // For readability
    // Can also do the database insertions here

    SpeciesInfoId speciesInfoId = readSpeciesInfoFromDatabase(); // Need to get the id after reading db, after it was primed
    // This would need to be in the thens for inserts in the app flow
    testState.interestingGivens().add(speciesInfoId);
  }

  private SpeciesInfoId readSpeciesInfoFromDatabase() {
    Optional<Record5<Integer, Integer, String, Float, Integer>> result = dslContext.select(
        SPECIFIESINFO.SPECIES_ID,
        SPECIFIESINFO.PERSON_ID,
        SPECIFIESINFO.SPECIES,
        SPECIFIESINFO.AVG_HEIGHT,
        SPECIFIESINFO.LIFESPAN)
        .from(SPECIFIESINFO).fetchOptional();
    SpeciesInfoRecord speciesInfo = result.map(record -> new SpeciesInfoRecord(
        new SpeciesInfoId(record.component1()),
        record.component2(),
        record.component3(),
        record.component4(),
        record.component5())).orElseThrow(IllegalStateException::new);
    // Test log
    testState.log(format("Species Info primed in database for speciesInfo id '%s'", speciesInfo.getSpeciesInfoId()), speciesInfo);
    return speciesInfo.getSpeciesInfoId();
  }
}
