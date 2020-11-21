package acceptancetests.reqandresponly.tests;

import acceptancetests.reqandresponly.testinfrastructure.AcceptanceTest;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.Test;

import java.util.List;

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

  @Override
  public List<Participant> participants() {
   // This is ordered in terms of how the diagram is generated
    return List.of(CLIENT_ACTOR, APP_PARTICIPANT);
  }
}
