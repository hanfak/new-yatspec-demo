package acceptancetests._01reqandresponly.tests;

import acceptancetests._01reqandresponly.testinfrastructure.AcceptanceTest;
import acceptancetests._01reqandresponly.thens.ThenTheResponseVersion3;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.List;

import static acceptancetests._01reqandresponly.testinfrastructure.YatspecConstants.APP_PARTICIPANT;
import static acceptancetests._01reqandresponly.testinfrastructure.YatspecConstants.CLIENT_ACTOR;
import static acceptancetests._01reqandresponly.testinfrastructure.YatspecConstants.RESPONSE_FROM_APP_TO_CLIENT;

@SuppressWarnings("SameParameterValue") // For test readability
class UsecaseOneMoreExamplesTest extends AcceptanceTest implements WithParticipants {

  @Test
  @Notes("Using a builder to create a fluent when")
  void shouldReturnResponseVersionOne() throws Exception {
    whenARequest
        .withUri("http://localhost:2222/usecaseone")
        .isCalledUsingHttpGetMethod();

    thenResponse.hasStatusCode().isEqualTo(200);
    thenResponse.hasContentType().isEqualTo("text/html");
    thenResponse.hasBody().isEqualTo("Hello, World");
  }

  @Test
  @Notes("Using a custom Assertj assertions to create fluent then, with custom assertion error")
  void shouldReturnResponseVersionTwo() throws Exception {
    whenARequest
        .withUri("http://localhost:2222/usecaseone")
        .isCalledUsingHttpGetMethod();

    thenReturnedResponse
        .hasStatusCode(200)
        .hasContentType("text/html")
        .hasBody("Hello, World");
  }

  @Test
  @Notes("Using a response stored in yatsepc test log")
  void shouldReturnResponseVersionThree() throws Exception {
    whenARequest
        .withUri("http://localhost:2222/usecaseone")
        .isCalledUsingHttpGetMethod();

    thenResponseReceived()
        .hasStatusCode(200)
        .hasContentType("text/html")
        .hasBody("Hello, World");
  }

  private ThenTheResponseVersion3 thenResponseReceived() {
    HttpResponse<String> response =  testState.getType(RESPONSE_FROM_APP_TO_CLIENT, HttpResponse.class);
    return new ThenTheResponseVersion3(testState, response);
  }

  @Override
  public List<Participant> participants() {
    // This is ordered in terms of how the diagram is generated
    return List.of(CLIENT_ACTOR, APP_PARTICIPANT);
  }
}
