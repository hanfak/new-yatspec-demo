package acceptancetests._03thirdpartyhttpcall.tests;

import acceptancetests._03thirdpartyhttpcall.testinfrastructure.AcceptanceTest;
import acceptancetests._03thirdpartyhttpcall.thens.ThenTheResponseVersion3;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.List;

import static acceptancetests._03thirdpartyhttpcall.testinfrastructure.YatspecConstants.APP_PARTICIPANT;
import static acceptancetests._03thirdpartyhttpcall.testinfrastructure.YatspecConstants.CLIENT_ACTOR;
import static acceptancetests._03thirdpartyhttpcall.testinfrastructure.YatspecConstants.RESPONSE_FROM_APP_TO_CLIENT;
import static acceptancetests._03thirdpartyhttpcall.testinfrastructure.YatspecConstants.STAR_WARS_SERVICE_PARTICIPANT;


@SuppressWarnings("SameParameterValue") // For test readability
class ExternalCallExampleOneTest extends AcceptanceTest implements WithParticipants {

  @Test
  @Notes("Using a response stored in yatsepc test log")
  void shouldReturnResponseVersionThree() throws Exception {
    givenStarWarsCharacterInformationService
        .forPersonId(1)
        .withBirthYear("777BBY")
        .withName("bobo")
        .willRespondWithSuccessfully();

    whenARequest
        .withUri("http://localhost:2222/externalCallExampleOneServlet/1")
        .isCalledUsingHttpGetMethod();

    thenResponseReceived()
        .hasStatusCode(200)
        .hasContentType("application/json")
        .hasBody("{\"personId\":\"1\",\"details\":{\"name\":\"BOBO\",\"birthYear\":\"777BBY\"}}");
    // assert on the request made
  }

  private ThenTheResponseVersion3 thenResponseReceived() {
    HttpResponse<String> response =  testState.getType(RESPONSE_FROM_APP_TO_CLIENT, HttpResponse.class);
    return new ThenTheResponseVersion3(testState, response);
  }

  @Override
  public List<Participant> participants() {
    // This is ordered in terms of how the diagram is generated
    return List.of(CLIENT_ACTOR, APP_PARTICIPANT, STAR_WARS_SERVICE_PARTICIPANT);
  }
}
