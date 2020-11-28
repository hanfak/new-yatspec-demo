package acceptancetests._02databasepriming.tests.usecasetwo;

import acceptancetests._02databasepriming.givens.GivenTheDatabaseContainsVersion1;
import acceptancetests._02databasepriming.givens.SpeciesInfoRecord;
import acceptancetests._02databasepriming.testinfrastructure.AcceptanceTest;
import acceptancetests._02databasepriming.testinfrastructure.YatspecConstants;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.Test;

import java.util.List;

import static acceptancetests._02databasepriming.givens.SpeciesInfoRecord.SpeciesInfoRecordBuilder.speciesInfoRecord;

// see class acceptancetests/_02databasepriming/givens/GivenTheDatabaseContainsVersion1.java for more implementation details
public class UseCaseTwoExamples1Test extends AcceptanceTest implements WithParticipants {

  @Test
  void shouldReturnAResponseAfterAccessingDatabase() throws Exception {
    givenTheDatabaseContains()
        .aSpeciesInfo(record()
            .withPersonId(1)
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

  private GivenTheDatabaseContainsVersion1 givenTheDatabaseContains() {
    // This needs to be primed, but does not need to be seen in documentation (upto user)
    givenTheDatabaseContainsVersion1.aCharacterStored(1, "blah");
    return givenTheDatabaseContainsVersion1;
  }

  // For readability
  private SpeciesInfoRecord.SpeciesInfoRecordBuilder record() {
    // If need to prime this object, but not needed in test output, can do it here due to builder
    return speciesInfoRecord();
  }

  @Override
  public List<Participant> participants() {
    // This is ordered in terms of how the diagram is generated
    return List.of(YatspecConstants.CLIENT_ACTOR, YatspecConstants.APP_PARTICIPANT);
  }
}
