package acceptancetests._02databasepriming.givens;

import acceptancetests._02databasepriming.testinfrastructure.TestDataProvider;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.jooq.DSLContext;
import org.jooq.Record4;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.util.Optional;

import static java.lang.String.format;
import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.SPECIFIESINFO;

public class GivenTheDatabaseContainsVersion5 {

  private final TestState testState;
  private final TestDataProvider dataProvider;

  private CharacterInfoRecord characterInfoRecord;
  private SpeciesInfoRecord speciesInfoRecord;

  public GivenTheDatabaseContainsVersion5(TestState testState, TestDataProvider dataProvider) {
    this.testState = testState;
    this.dataProvider = dataProvider;
  }

  public GivenTheDatabaseContainsVersion5 aCharacterStored(Integer personId, String name) {
    dataProvider.addCharacter(personId,name);
    return this;
  }

  public GivenTheDatabaseContainsVersion5 hasASpeciesInfo(SpeciesInfoRecord.SpeciesInfoRecordBuilder builder) {
    this.speciesInfoRecord = builder.build();
    return this;
  }

  public GivenTheDatabaseContainsVersion5 hasCharacterInfo(CharacterInfoRecord.CharacterInfoRecordBuilder characterInfoRecordBuilder) {
    this.characterInfoRecord = characterInfoRecordBuilder.build();
    return this;
  }

  public void isStoredInTheDatabase() {
    // For readability
    // Can also do the database insertions here
    primeSpeciesInfoTableInDatabase();
    primeCharacterInfoTableInDatabase();
  }

  private void primeCharacterInfoTableInDatabase() {
    int characterInfoId = dataProvider.addCharacterInfo(characterInfoRecord.getPersonId(), characterInfoRecord.getPersonName(), characterInfoRecord.getBirthYear());
    testState.interestingGivens().add("characterInfoId", characterInfoId);
    CharacterInfoRecord characterInfo = dataProvider.readCharacterInfoFromDatabase();
    testState.log(format("Character Info primed in database for characterInfoId '%s'", characterInfo.getCharacterInfoId()), characterInfo);
  }

  private void primeSpeciesInfoTableInDatabase() {
    int expectedSpeciesInfoId = dataProvider.addSpeciesInfo(speciesInfoRecord.getPersonId(), speciesInfoRecord.getName(), speciesInfoRecord.getAverageHeight(), speciesInfoRecord.getLifespan());
    testState.interestingGivens().add(new SpeciesInfoId(expectedSpeciesInfoId));
    SpeciesInfoRecord speciesInfoRecord = dataProvider.readSpeciesInfoFromDatabase();
    testState.log(format("Species Info primed in database for speciesInfo id '%s'", speciesInfoRecord.getSpeciesInfoId()), speciesInfoRecord);
  }
}
