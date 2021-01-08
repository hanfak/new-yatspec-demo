package acceptancetests._02databasepriming.tests.usecasetwo;

import acceptancetests._02databasepriming.givens.GivenTheDatabaseContainsVersion4;
import acceptancetests._02databasepriming.givens.SpeciesInfoId;
import acceptancetests._02databasepriming.givens.SpeciesInfoRecord;
import acceptancetests._02databasepriming.testinfrastructure.AcceptanceTest;
import acceptancetests._02databasepriming.testinfrastructure.YatspecConstants;
import acceptancetests._02databasepriming.testinfrastructure.renderers.CustomJavaSourceRenderer;
import acceptancetests._02databasepriming.testinfrastructure.renderers.HttpRequestRenderer;
import acceptancetests._02databasepriming.testinfrastructure.renderers.HttpResponseRenderer;
import acceptancetests._02databasepriming.testinfrastructure.renderers.SpeciesInfoInDatabaseRendererVersion2;
import acceptancetests._02databasepriming.thens.ThenTheDatabaseContains;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.Test;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

import static acceptancetests._02databasepriming.givens.SpeciesInfoRecord.SpeciesInfoRecordBuilder.speciesInfoRecord;

// see class acceptancetests/_02databasepriming/givens/GivenTheDatabaseContainsVersion4.java for more implementation details
public class UseCaseTwoExamples4Test extends AcceptanceTest implements WithParticipants {
  @Notes("This test demonstrates the use of asserting on the database, using the builder pattern and reading from db")
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

    thenTheDatabaseContainsARecord()
        .forSpeciesInfoId(generatedId())
        .forPersonId(1)
        .forName("Ogier")
        .forLifespan(500)
        .forAverageHeight(3.5F)
        .wasSuccessfullyPersisted();

  }
  private Integer generatedId() {
    // Use interetsting givens, to get the generated id, depending on how it was set,
    // need to get from database in the givens or thens
    return testState.interestingGivens().getType(SpeciesInfoId.class).getValue();
  }

  private GivenTheDatabaseContainsVersion4 givenTheDatabaseContains() {
    testState.interestingGivens().add("personId", 1);
    testState.interestingGivens().add("name", "blah");
    givenTheDatabaseContainsVersion4.aCharacterStored(
        testState.interestingGivens().getType("personId", Integer.class),
        testState.interestingGivens().getType("name", String.class));

    return givenTheDatabaseContainsVersion4;
  }

  // For readability
  private SpeciesInfoRecord.SpeciesInfoRecordBuilder record() {
    return speciesInfoRecord();
  }

  private ThenTheDatabaseContains thenTheDatabaseContainsARecord() {
    return thenTheDatabaseContains;
  }

  @Override
  public List<Participant> participants() {
    return List.of(YatspecConstants.CLIENT_ACTOR, YatspecConstants.APP_PARTICIPANT);
  }

  // This is normally in AcceptanceTest class, but only wanted new renderer for this test
  @Override
  public Collection<SpecResultListener> getResultListeners() throws Exception {
    return List.of(
        new HtmlResultRenderer()
            .withCustomRenderer(HttpRequest.class, result ->new HttpRequestRenderer())
            .withCustomRenderer(HttpResponse.class, result ->new HttpResponseRenderer())
            .withCustomRenderer(JavaSource.class, result ->new CustomJavaSourceRenderer())
            // This looks in capturedInputs for this class type, and uses the renderer to display it correctly in output
            .withCustomRenderer(SpeciesInfoRecord.class, result ->new SpeciesInfoInDatabaseRendererVersion2())
            .withCustomRenderer(SvgWrapper.class, result ->new DontHighlightRenderer<>()),
        new HtmlIndexRenderer()
    );
  }
}
