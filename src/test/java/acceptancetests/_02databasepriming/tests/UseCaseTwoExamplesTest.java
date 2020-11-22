package acceptancetests._02databasepriming.tests;

import acceptancetests._02databasepriming.givens.GivenTheDatabaseContains;
import acceptancetests._02databasepriming.givens.SpeciesInfoRecord;
import acceptancetests._02databasepriming.testinfrastructure.AcceptanceTest;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.Test;

import java.util.List;

import static acceptancetests._02databasepriming.givens.SpeciesInfoRecord.SpeciesInfoRecordBuilder.speciesInfoRecord;

public class UseCaseTwoExamplesTest extends AcceptanceTest implements WithParticipants {

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

  private GivenTheDatabaseContains givenTheDatabaseContains() {
    // This needs to be primed, but does not need to be seen in documentation (upto user)
    givenTheDatabaseContains.aCharacterStored(1, "blah");
    return givenTheDatabaseContains;
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
