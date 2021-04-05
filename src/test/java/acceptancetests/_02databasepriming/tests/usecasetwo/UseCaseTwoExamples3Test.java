package acceptancetests._02databasepriming.tests.usecasetwo;

import acceptancetests._02databasepriming.givens.GivenTheDatabaseContainsVersion3;
import acceptancetests._02databasepriming.givens.SpeciesInfoRecord;
import acceptancetests._02databasepriming.testinfrastructure.AcceptanceTest;
import acceptancetests._02databasepriming.testinfrastructure.YatspecConstants;
import acceptancetests._02databasepriming.testinfrastructure.renderers.SpeciesInfoInDatabaseRenderer;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.parsing.TestText;
import com.googlecode.yatspec.plugin.diagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.Test;
import testinfrastructure.renderers.CustomJavaSourceRenderer;
import testinfrastructure.renderers.HttpRequestRenderer;
import testinfrastructure.renderers.HttpResponseRenderer;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

import static acceptancetests._02databasepriming.givens.SpeciesInfoRecord.SpeciesInfoRecordBuilder.speciesInfoRecord;

// see class acceptancetests/_02databasepriming/givens/GivenTheDatabaseContainsVersion3.java for more implementation details
public class UseCaseTwoExamples3Test extends AcceptanceTest implements WithParticipants {
  @Notes("This test demonstrates the use of rendering the primed database in yatspec output,\n" +
      "Use of custom renderer to display special formating depending on the object type in the test state (interesting givens or captured inputs)" +
      "useful for priming knowing state of db, to prime manually when running black box testing")
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

  private GivenTheDatabaseContainsVersion3 givenTheDatabaseContains() {
    testState.interestingGivens().add("personId", 1);
    testState.interestingGivens().add("name", "blah");
    givenTheDatabaseContainsVersion3.aCharacterStored(
        testState.interestingGivens().getType("personId", Integer.class),
        testState.interestingGivens().getType("name", String.class));

    return givenTheDatabaseContainsVersion3;
  }

  // For readability
  private SpeciesInfoRecord.SpeciesInfoRecordBuilder record() {
    return speciesInfoRecord();
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
            .withCustomRenderer(HttpRequest.class, result -> new HttpRequestRenderer())
            .withCustomRenderer(HttpResponse.class, result -> new HttpResponseRenderer())
            .withCustomRenderer(TestText.class, result -> new CustomJavaSourceRenderer())
            // This looks in capturedInputs for this class type, and uses the renderer to display it correctly in output
            .withCustomRenderer(SpeciesInfoRecord.class, result -> new SpeciesInfoInDatabaseRenderer())
            .withCustomRenderer(SvgWrapper.class, result -> new DontHighlightRenderer<>()),
        new HtmlIndexRenderer()
    );
  }
}
