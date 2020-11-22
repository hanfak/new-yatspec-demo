package acceptancetests._02databasepriming.givens;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;

public class GivenTheDatabaseContains {

  private final DataSource dataSource;

  public GivenTheDatabaseContains(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public GivenTheDatabaseContains aSpeciesInfo(SpeciesInfoRecord.SpeciesInfoRecordBuilder builder){
    SpeciesInfoRecord speciesInfoRecord = builder.build();
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    dslContext.insertInto(SPECIFIESINFO)
            .set(SPECIFIESINFO.PERSON_ID, speciesInfoRecord.getId())
            .set(SPECIFIESINFO.SPECIES, speciesInfoRecord.getName())
            .set(SPECIFIESINFO.AVG_HEIGHT, speciesInfoRecord.getAverageHeight())
            .set(SPECIFIESINFO.LIFESPAN, speciesInfoRecord.getLifespan())
            .execute();
    return this;
  }

  public GivenTheDatabaseContains aCharacterStored(Integer personId, String name){
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
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
