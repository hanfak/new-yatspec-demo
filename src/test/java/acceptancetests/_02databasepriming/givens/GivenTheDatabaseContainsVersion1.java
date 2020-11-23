package acceptancetests._02databasepriming.givens;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;

public class GivenTheDatabaseContainsVersion1 {

  private final DSLContext dslContext;

  // tODO extract to separate class dslcontextadpater
  public GivenTheDatabaseContainsVersion1(DataSource dataSource) {
    dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
  }

  public GivenTheDatabaseContainsVersion1 aSpeciesInfo(SpeciesInfoRecord.SpeciesInfoRecordBuilder builder) {
    SpeciesInfoRecord speciesInfoRecord = builder.build();
    dslContext.insertInto(SPECIFIESINFO)
        .set(SPECIFIESINFO.PERSON_ID, speciesInfoRecord.getPersonId())
        .set(SPECIFIESINFO.SPECIES, speciesInfoRecord.getName())
        .set(SPECIFIESINFO.AVG_HEIGHT, speciesInfoRecord.getAverageHeight())
        .set(SPECIFIESINFO.LIFESPAN, speciesInfoRecord.getLifespan())
        .execute();
    return this;
  }

  public GivenTheDatabaseContainsVersion1 aCharacterStored(Integer personId, String name) {
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
