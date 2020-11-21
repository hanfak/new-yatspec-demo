package acceptancetests.reqandresponly.tests;

import acceptancetests.reqandresponly.testinfrastructure.AcceptanceTest;
import acceptancetests.reqandresponly.thens.ThenTheResponse;
import com.googlecode.yatspec.junit.WithParticipants;
import com.googlecode.yatspec.sequence.Participant;
import org.junit.jupiter.api.Test;

import java.util.List;

@SuppressWarnings("SameParameterValue") // For test readability
class UsecaseOneTest extends AcceptanceTest implements WithParticipants {

  @Test
  void shouldReturnResponseVersionOne() throws Exception {
    whenARequestIsMadeTo.theUseCaseOne();

    thenTheResponse.hasStatusCode().isEqualTo(200); // TODO wrapperfor isEqualTo
    thenTheResponse.hasContentType().isEqualTo("text/html");
    thenTheResponse.hasBody().isEqualTo("Hello, World");
  }

  @Test
  void shouldReturnResponseVersionTwo() throws Exception {
    whenARequestIsMadeTo.theUseCaseOne();

    thenTheResponse.hasStatusCode().isEqualTo(200);
    andTheResponse().hasContentType().isEqualTo("text/html");
    andTheResponse().hasBody().isEqualTo("Hello, World");
  }

  @Test
  void shouldReturnResponseVersionThree() throws Exception {
    whenARequestIsMadeTo.theUseCaseOneWithUri("http://localhost:2222/usecaseone");

    thenTheResponse.hasStatusCode().isEqualTo(200);
    andTheResponse().hasContentType().isEqualTo("text/html");
    andTheResponse().hasBody().isEqualTo("Hello, World");
  }

  private ThenTheResponse andTheResponse() {
    return thenTheResponse;
  }

  @Override
  public List<Participant> participants() {
   // This is ordered in terms of how the diagram is generated
    return List.of(CLIENT_ACTOR, APP_PARTICIPANT);
  }
}
