package wiring;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.SPECIFIESINFO;
import static wiring.ApplicationWiring.APPLICATION_LOGGER;

public final class Main {

  private static final String PROD_PROPERTIES = "target/classes/application.prod.properties";

  public static void main(String... args) {
    Application application = new Application(PROD_PROPERTIES);
    cleanDatabase(application);
    populateDatabase(application);
    application.start();
  }

  private static void cleanDatabase(Application application) {
    APPLICATION_LOGGER.info("cleaning data");
    DSLContext dslContext = DSL.using(application.getDataSource(), SQLDialect.POSTGRES);
    dslContext.deleteFrom(CHARACTERINFO).execute();
    dslContext.deleteFrom(SPECIFIESINFO).execute();
  }

  // Some data to use in running app for some end points
  private static void populateDatabase(Application application) {
    APPLICATION_LOGGER.info("inserting data");
    DSLContext dslContext = DSL.using(application.getDataSource(), SQLDialect.POSTGRES);
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
