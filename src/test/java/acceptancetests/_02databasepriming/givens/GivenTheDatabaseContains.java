package acceptancetests._02databasepriming.givens;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;

public class GivenTheDatabaseContains {

  private final DataSource dataSource;
  private final DSLContext dslContext;

  // tODO extract to separate class dslcontextadpater
  public GivenTheDatabaseContains(DataSource dataSource) {
    this.dataSource = dataSource;
    dslContext = DSL.using(this.dataSource, SQLDialect.POSTGRES);
  }

  public GivenTheDatabaseContains aSpeciesInfo(SpeciesInfoRecord.SpeciesInfoRecordBuilder builder) {
    SpeciesInfoRecord speciesInfoRecord = builder.build();
    dslContext.insertInto(SPECIFIESINFO)
        .set(SPECIFIESINFO.PERSON_ID, speciesInfoRecord.getId())
        .set(SPECIFIESINFO.SPECIES, speciesInfoRecord.getName())
        .set(SPECIFIESINFO.AVG_HEIGHT, speciesInfoRecord.getAverageHeight())
        .set(SPECIFIESINFO.LIFESPAN, speciesInfoRecord.getLifespan())
        .execute();
    return this;
  }

  public GivenTheDatabaseContains aCharacterStored(Integer personId, String name) {
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
