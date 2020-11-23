package acceptancetests._02databasepriming.tests;

import acceptancetests._02databasepriming.givens.GivenTheDatabaseContainsVersion2;
import acceptancetests._02databasepriming.givens.SpeciesInfoRecord;
import acceptancetests._02databasepriming.testinfrastructure.AcceptanceTest;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.Test;

import java.util.List;

import static acceptancetests._02databasepriming.givens.SpeciesInfoRecord.SpeciesInfoRecordBuilder.speciesInfoRecord;

// see class acceptancetests/_02databasepriming/givens/GivenTheDatabaseContainsVersion1.java for more implementation details
public class UseCaseTwoExamples3Test extends AcceptanceTest implements WithParticipants {
  @Notes("This test demonstrates the use of intereting givens feature in yatspec")
  @Test
  void shouldReturnAResponseAfterAccessingDatabase() throws Exception {
    givenTheDatabaseContains()
        .aSpeciesInfo(record()
            .withId(1)
            .withName("Ogier")
            .withAverageHeight(3.5F)
            .withLifespan(500))
        .isStoredInTheDatabase();

    whenARequest
        .withUri("http://localhost:2222/usecasetwo")
        .isCalledUsingHttpGetMethod();

    thenReturnedResponse
        .hasStatusCode(200)
        .hasContentType("text/html")
        .hasBody("Hello, Ogier, who lives for 500 years and has average height of 3.5 metres");
  }

  private GivenTheDatabaseContainsVersion2 givenTheDatabaseContains() {
    testState.interestingGivens().add("personId", 1);
    testState.interestingGivens().add("name", "blah");
    // When in interesting given (a map) can extract them out,
    // especially useful if generating random primings
    givenTheDatabaseContainsVersion2.aCharacterStored(
        testState.interestingGivens().getType("personId", Integer.class),
        testState.interestingGivens().getType("name", String.class));

    return givenTheDatabaseContainsVersion2;
  }

  // For readability
  private SpeciesInfoRecord.SpeciesInfoRecordBuilder record() {
    // If need to prime this object, but not needed in test output, can do it here due to builder
    return speciesInfoRecord();
  }

  @Override
  public List<Participant> participants() {
    // This is ordered in terms of how the diagram is generated
    return List.of(CLIENT_ACTOR, APP_PARTICIPANT);
  }
}
