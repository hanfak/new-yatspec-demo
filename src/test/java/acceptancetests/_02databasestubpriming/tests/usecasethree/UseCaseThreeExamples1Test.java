package acceptancetests._02databasestubpriming.tests.usecasethree;

import acceptancetests._02databasestubpriming.givens.GivenTheDatabaseContains;
import acceptancetests._02databasestubpriming.testinfrastructure.AcceptanceTest;
import acceptancetests._02databasestubpriming.testinfrastructure.YatspecConstants;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.CharacterInfoRecord;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.SpeciesInfoId;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.SpeciesInfoRecord;
import acceptancetests._02databasestubpriming.thens.ThenTheDatabaseContains;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.Test;

import java.util.List;

import static acceptancetests._02databasestubpriming.testinfrastructure.stub.CharacterInfoRecord.CharacterInfoRecordBuilder.characterInfoRecord;
import static acceptancetests._02databasestubpriming.testinfrastructure.stub.SpeciesInfoRecord.SpeciesInfoRecordBuilder.speciesInfoRecord;


// see class acceptancetests/_02databasepriming/givens/GivenTheDatabaseContainsVersion4.java for more implementation details
public class UseCaseThreeExamples1Test extends AcceptanceTest implements WithParticipants {

  @Notes("This test demonstrates the multiple tables being primed in smae given. While asserted on in different thens")
  @Test
  void shouldReturnAResponseAfterAccessingDatabase() throws Exception {
    givenTheDatabaseContains()
        .hasCharacterInfo(data()
            .withPersonId(12345)
            .withBirthYear("1502")
            .withPersonName("Loial"))
        .hasASpeciesInfo(record()
            .withPersonId(12345)
            .withName("Ogier")
            .withAverageHeight(3.5F)
            .withLifespan(500))
        .isStoredInTheDatabase();

    whenARequest
        .withUri("http://localhost:2222/usecasethree/12345")
        .isCalledUsingHttpGetMethod();

    thenTheSpeciesInfoDatabaseContainsARecord()
        .withSpeciesInfo(record()
            .withId(generatedId())
            .withSpeciesInfoId(generatedId())
            .withPersonId(12345)
            .withName("Ogier")
            .withLifespan(500)
            .withAverageHeight(3.5F))
        .wasInsertedIntoTheDatabase();
    thenTheCharacterInfoDatabaseContains.aCharacterInfo(data()
        .withId(generatedCharacterInfoId())
        .withCharacterInfoId(generatedCharacterInfoId())
        .withPersonId(12345)
        .withBirthYear("1502")
        .withPersonName("Loial"))
        .wasInsertedIntoTheDatabase();
    thenReturnedResponse
        .hasStatusCode(200)
        .hasContentType("text/html")
        .hasBody("Hello, Ogier, who lives for 500 years and has average height of 3.5 metres, and was born 1502");
  }

  private GivenTheDatabaseContains givenTheDatabaseContains() {
    testState.interestingGivens().add("personId", 12345);
    testState.interestingGivens().add("name", "Loial");
    return givenTheDatabaseContains.aCharacterStored(12345, "Loial");
  }

  // For readability
  private SpeciesInfoRecord.SpeciesInfoRecordBuilder record() {
    return speciesInfoRecord();
  }

  // For readability
  private CharacterInfoRecord.CharacterInfoRecordBuilder data() {
    return characterInfoRecord();
  }

  private Integer generatedId() {
    return testState.interestingGivens().getType(SpeciesInfoId.class).getValue();
  }

  private Integer generatedCharacterInfoId() {
    return testState.interestingGivens().getType("characterInfoId", Integer.class);
  }

  private ThenTheDatabaseContains thenTheSpeciesInfoDatabaseContainsARecord() {
    return thenTheDatabaseContains;
  }

  @Override
  public List<Participant> participants() {
    return List.of(YatspecConstants.CLIENT_ACTOR, YatspecConstants.APP_PARTICIPANT);
  }
}
