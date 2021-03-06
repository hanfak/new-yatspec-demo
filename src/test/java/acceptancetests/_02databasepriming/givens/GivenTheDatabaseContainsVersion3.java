package acceptancetests._02databasepriming.givens;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.jooq.DSLContext;
import org.jooq.Record5;

import java.util.Optional;

import static java.lang.String.format;
import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;

public class GivenTheDatabaseContainsVersion3 {

  private final DSLContext dslContext;
  private final TestState testState;

  public GivenTheDatabaseContainsVersion3(TestState testState, DSLContext dslContext) {
    this.testState = testState;
    this.dslContext = dslContext;
  }

  public GivenTheDatabaseContainsVersion3 aSpeciesInfo(SpeciesInfoRecord.SpeciesInfoRecordBuilder builder) {
    SpeciesInfoRecord speciesInfoRecord = builder.build();
    testState.interestingGivens().add(speciesInfoRecord); // As this is of type which matches on renderer, then interesting givens will also be rendered
    testState.interestingGivens().add("speciesInfoRecord object used to prime in database", speciesInfoRecord); // Even if you set the key, this will still be rendered
    dslContext.insertInto(SPECIFIESINFO)
        .set(SPECIFIESINFO.PERSON_ID, speciesInfoRecord.getPersonId())
        .set(SPECIFIESINFO.SPECIES, speciesInfoRecord.getName())
        .set(SPECIFIESINFO.AVG_HEIGHT, speciesInfoRecord.getAverageHeight())
        .set(SPECIFIESINFO.LIFESPAN, speciesInfoRecord.getLifespan())
        .execute();
    return this;
  }

  public GivenTheDatabaseContainsVersion3 aCharacterStored(Integer personId, String name) {
    dslContext.insertInto(CHARACTERS)
        .set(CHARACTERS.PERSON_ID, personId)
        .set(CHARACTERS.PERSON_NAME, name)
        .execute();
    return this;
  }

  public void isStoredInTheDatabase() {
    // For readability
    // Can also do the database insertions here

    // We can use the database methods (test or prod) to read from database and add to testState.capturedInputs
    //    Which will be displayed in yatspec output, via the custom renderer SpeciesInfoInDatabaseRenderer.class
    // This can lead to extra database calls during test invocation. But if database contents is being tested,
    //    then db reads are already happening. So can read from db and cache the result here, then use the cached
    //    result in the thens (for asserting on)
    readSpeciesInfoFromDatabase();
  }

  private void readSpeciesInfoFromDatabase() {
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
    // Test log, adds info to an encapsulated map which is rendered as html and can be used to assert on
    testState.log(format("Species Info primed in database for speciesInfo id '%s'", speciesInfo.getSpeciesInfoId()), speciesInfo);
    // You can say as this is primed in the givens, it should be stored in the interesting givens only.
    //   AS the object used for rendering SpeciesInfoRecord, matches what is in the database, it is show in both
    //   But speciesinfoid is null, as not primed
    //   Using just the object SpeciesInfoRecord as passed in on line 28, is not feasible as we do not know the speciesInfoId as it is generated by databse, so will need to read it
    // But it depends on the reader on how or where they want to see the database output
    // ie
    testState.interestingGivens().add(format("Species Info primed in database for speciesInfo id '%s'", speciesInfo.getSpeciesInfoId()), speciesInfo);
  }
}
