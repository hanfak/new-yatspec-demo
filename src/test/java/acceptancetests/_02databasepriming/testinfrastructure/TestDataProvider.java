package acceptancetests._02databasepriming.testinfrastructure;

import acceptancetests._02databasepriming.givens.CharacterInfoRecord;
import acceptancetests._02databasepriming.givens.SpeciesInfoId;
import acceptancetests._02databasepriming.givens.SpeciesInfoRecord;
import adapters.logging.LoggingCategory;
import org.jooq.DSLContext;
import org.jooq.Record4;
import org.jooq.Record5;
import org.slf4j.Logger;

import java.util.Optional;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;
import static org.slf4j.LoggerFactory.getLogger;

// TODO extend CharacterDataProvider, which is newed when app is started in test context
// TODO need to seperate factories into new class, n pass into app.start
//TODO settings for different db name, so prod is not used
public class TestDataProvider {

  static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  private final DSLContext dslContext;

  public TestDataProvider(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public void deleteAllInfoFromAllTables() {
    APPLICATION_LOGGER.info("Deleting database before test");
    dslContext.deleteFrom(CHARACTERINFO).execute();
    dslContext.deleteFrom(SPECIFIESINFO).execute();
    dslContext.deleteFrom(CHARACTERS).execute();
    APPLICATION_LOGGER.info("Database clean before test execution");

  }

  public void addCharacter(Integer personId, String name) {
    dslContext.insertInto(CHARACTERS)
        .set(CHARACTERS.PERSON_ID, personId)
        .set(CHARACTERS.PERSON_NAME, name)
        .execute();
  }

  public int addCharacterInfo(Integer personId, String personName, String birthYear) {
    return dslContext.insertInto(CHARACTERINFO)
        .set(CHARACTERINFO.PERSON_ID, personId)
        .set(CHARACTERINFO.PERSON_NAME, personName)
        .set(CHARACTERINFO.BIRTH_YEAR, birthYear)
        .returningResult(CHARACTERINFO.CHARACTER_INFO_ID)
        .fetchOne()
        .component1();
  }

  public Integer addSpeciesInfo(Integer personId, String name, Float averageHeight, Integer lifespan) {
    return dslContext.insertInto(SPECIFIESINFO)
        .set(SPECIFIESINFO.PERSON_ID, personId)
        .set(SPECIFIESINFO.SPECIES, name)
        .set(SPECIFIESINFO.AVG_HEIGHT, averageHeight)
        .set(SPECIFIESINFO.LIFESPAN, lifespan)
        .returningResult(SPECIFIESINFO.SPECIES_ID)
        .fetchOne()
        .component1();
  }

  // TODO: use speciesid as arg, need duplicate one for given (no arg) and one for then (arg
  public SpeciesInfoRecord readSpeciesInfoFromDatabase() {
    Optional<Record5<Integer, Integer, String, Float, Integer>> result = dslContext.select(
        SPECIFIESINFO.SPECIES_ID,
        SPECIFIESINFO.PERSON_ID,
        SPECIFIESINFO.SPECIES,
        SPECIFIESINFO.AVG_HEIGHT,
        SPECIFIESINFO.LIFESPAN)
        .from(SPECIFIESINFO).fetchOptional();
    return result.map(record -> new SpeciesInfoRecord(
        new SpeciesInfoId(record.component1()),
        record.component2(),
        record.component3(),
        record.component4(),
        record.component5())).orElseThrow(IllegalStateException::new);
  }

  // TODO: use speciesid as arg, need duplicate one for given (no arg) and one for then (arg
  public CharacterInfoRecord readCharacterInfoFromDatabase() {
    Optional<Record4<Integer, Integer, String, String>> result = dslContext.select(
        CHARACTERINFO.CHARACTER_INFO_ID, CHARACTERINFO.PERSON_ID, CHARACTERINFO.BIRTH_YEAR, CHARACTERINFO.PERSON_NAME)
        .from(CHARACTERINFO).fetchOptional();
    CharacterInfoRecord characterInfo = result.map(record -> new CharacterInfoRecord(
        record.component1(),
        record.component2(),
        record.component3(),
        record.component4())).orElseThrow(IllegalStateException::new);
    return characterInfo;
  }
}
