package adapters.databaseservice;

import adapters.webserver.servlets.DataProvider;
import core.domain.Person;
import core.domain.SpeciesInfo;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record3;

import java.util.Optional;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;

public class CharacterDataProvider implements DataProvider {

  // TODO inject logger, and use in methods
  private final DSLContext dslContext;

  // tODO split methods out
  public CharacterDataProvider(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Override
  public Integer getPersonId(String personName) {
    Optional<Record1<Integer>> result = dslContext.select(CHARACTERS.PERSON_ID)
        .from(CHARACTERS)
        .where(CHARACTERS.PERSON_NAME.eq(personName))
        .fetchOptional();
    System.out.println("*********** person name : " + personName);
    System.out.println("*********** record : " + result.map(Record1::component1).orElse(0));
    return result.map(Record1::component1).orElse(0);
  }

  @Override
  public void storeCharacterInfo(String personId, Person characterInfo) {
    dslContext.insertInto(CHARACTERINFO)
        .set(CHARACTERINFO.PERSON_ID, Integer.parseInt(personId))
        .set(CHARACTERINFO.BIRTH_YEAR, characterInfo.getBirthYear())
        .set(CHARACTERINFO.PERSON_NAME, characterInfo.getName())
        .execute();
  }

  @Override
  public SpeciesInfo getSpeciesInfo(Integer personId) {
    Optional<Record3<String, Float, Integer>> result = dslContext.select(SPECIFIESINFO.SPECIES, SPECIFIESINFO.AVG_HEIGHT, SPECIFIESINFO.LIFESPAN)
        .from(SPECIFIESINFO)
        .where(SPECIFIESINFO.PERSON_ID.eq(personId))
        .fetchOptional();
    String name = result.get().field1().getName();
    System.out.println("name = " + name);
    return result.map(record -> new SpeciesInfo(record.component1(), record.component3(), record.component2())).orElseThrow(IllegalStateException::new);
  }

  @Override
  public String getBirthYear(int personId) {
    Optional<Record1<String>> result = dslContext.select(CHARACTERINFO.BIRTH_YEAR)
        .from(CHARACTERINFO)
        .where(CHARACTERINFO.PERSON_ID.eq(personId))
        .fetchOptional();
    return result.orElseThrow(IllegalStateException::new).component1();
  }
}
