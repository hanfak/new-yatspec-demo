package acceptancetests._02databasepriming.tests.usecasethree;

import acceptancetests._02databasepriming.givens.CharacterInfoRecord;
import acceptancetests._02databasepriming.givens.GivenTheDatabaseContainsVersion5;
import acceptancetests._02databasepriming.givens.SpeciesInfoId;
import acceptancetests._02databasepriming.givens.SpeciesInfoRecord;
import acceptancetests._02databasepriming.testinfrastructure.AcceptanceTest;
import acceptancetests._02databasepriming.testinfrastructure.YatspecConstants;
import acceptancetests._02databasepriming.testinfrastructure.renderers.CharacterInfoInDatabaseRenderer;
import acceptancetests._02databasepriming.testinfrastructure.renderers.SpeciesInfoInDatabaseRendererVersion2;
import acceptancetests._02databasepriming.thens.ThenTheDatabaseContains;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.parsing.TestText;
import com.googlecode.yatspec.plugin.diagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testinfrastructure.renderers.CustomJavaSourceRenderer;
import testinfrastructure.renderers.HttpRequestRenderer;
import testinfrastructure.renderers.HttpResponseRenderer;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

import static acceptancetests._02databasepriming.givens.CharacterInfoRecord.CharacterInfoRecordBuilder.characterInfoRecord;
import static acceptancetests._02databasepriming.givens.SpeciesInfoRecord.SpeciesInfoRecordBuilder.speciesInfoRecord;

// see class acceptancetests/_02databasepriming/givens/GivenTheDatabaseContainsVersion4.java for more implementation details
public class UseCaseThreeExamples1Test extends AcceptanceTest implements WithParticipants {
  @Notes("This test demonstrates the multiple tables being primed in smae given. While asserted on in different thens")
  @Test
  @DisplayName("hello")
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
          .withSpeciesInfoId(generatedId())
          .withPersonId(12345)
          .withName("Ogier")
          .withLifespan(500)
          .withAverageHeight(3.5F))
        .wasInsertedIntoTheDatabase();
    thenTheCharacterInfoDatabaseContains.aCharacterInfo(data()
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

  private GivenTheDatabaseContainsVersion5 givenTheDatabaseContains() {
    testState.interestingGivens().add("personId", 12345);
    testState.interestingGivens().add("name", "Loial");
    return givenTheDatabaseContainsVersion5.aCharacterStored(12345, "Loial");
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

  // This is normally in AcceptanceTest class, but only wanted new renderer for this test
  @Override
  public Collection<SpecResultListener> getResultListeners() throws Exception {
    return List.of(
        new HtmlResultRenderer()
            .withCustomRenderer(HttpRequest.class, result -> new HttpRequestRenderer())
            .withCustomRenderer(HttpResponse.class, result -> new HttpResponseRenderer())
            .withCustomRenderer(TestText.class, result -> new CustomJavaSourceRenderer())
            .withCustomRenderer(SpeciesInfoRecord.class, result -> new SpeciesInfoInDatabaseRendererVersion2())
            .withCustomRenderer(CharacterInfoRecord.class, result -> new CharacterInfoInDatabaseRenderer())
            .withCustomRenderer(SvgWrapper.class, result -> new DontHighlightRenderer<>()),
        new HtmlIndexRenderer()
    );
  }
}
