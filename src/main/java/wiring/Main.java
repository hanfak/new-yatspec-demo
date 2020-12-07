package wiring;

import adapters.logging.LoggingCategory;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;
import static org.slf4j.LoggerFactory.getLogger;

public final class Main {

  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());
  // Could use java command line env variable to set this
  private static final String PROD_PROPERTIES = "target/classes/application.prod.properties";

  public static void main(String... args) {
    Application application = new Application(PROD_PROPERTIES, APPLICATION_LOGGER);
    setupData(application); // Would not be used in production, if need data would be done via sql scripts in flyway migration
    application.start();
  }

  private static void setupData(Application application) {
    DSLContext dslContext = DSL.using(application.getDataSource(), SQLDialect.POSTGRES);
    cleanDatabase(dslContext);
    populateDatabase(dslContext);
  }

  private static void cleanDatabase(DSLContext dslContext) {
    APPLICATION_LOGGER.info("cleaning data");
    dslContext.deleteFrom(CHARACTERINFO).execute();
    dslContext.deleteFrom(SPECIFIESINFO).execute();
    dslContext.deleteFrom(CHARACTERS).execute();
  }

  // Some data to use in running app for some end points
  private static void populateDatabase(DSLContext dslContext) {
    APPLICATION_LOGGER.info("inserting data");
    dslContext.insertInto(CHARACTERS)
        .set(CHARACTERS.PERSON_ID, 1)
        .set(CHARACTERS.PERSON_NAME, "Luke Skywalker")
        .execute();
    dslContext.insertInto(SPECIFIESINFO)
        .set(SPECIFIESINFO.PERSON_ID, 1)
        .set(SPECIFIESINFO.SPECIES, "human")
        .set(SPECIFIESINFO.AVG_HEIGHT, 6.5F)
        .set(SPECIFIESINFO.LIFESPAN, 80)
        .execute();
    dslContext.insertInto(CHARACTERINFO)
        .set(CHARACTERINFO.PERSON_ID, 1)
        .set(CHARACTERINFO.PERSON_NAME, "Luke Skywalker")
        .set(CHARACTERINFO.BIRTH_YEAR, "1000")
        .execute();
    dslContext.insertInto(CHARACTERS)
        .set(CHARACTERS.PERSON_ID, 20)
        .set(CHARACTERS.PERSON_NAME, "Yoda")
        .execute();
    dslContext.insertInto(SPECIFIESINFO)
        .set(SPECIFIESINFO.PERSON_ID, 20)
        .set(SPECIFIESINFO.SPECIES, "yoda")
        .set(SPECIFIESINFO.AVG_HEIGHT, 2.3F)
        .set(SPECIFIESINFO.LIFESPAN, 800)
        .execute();
    dslContext.insertInto(CHARACTERINFO)
        .set(CHARACTERINFO.PERSON_ID, 20)
        .set(CHARACTERINFO.PERSON_NAME, "yoda")
        .set(CHARACTERINFO.BIRTH_YEAR, "20")
        .execute();
  }
}
