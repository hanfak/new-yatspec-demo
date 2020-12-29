package acceptancetests._03thirdpartyhttpcall.thens;

import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.net.http.HttpRequest;

import static acceptancetests._03thirdpartyhttpcall.testinfrastructure.YatspecConstants.REQUEST_FROM_APP_TO_STAR_WARS_SERVICE;
import static org.assertj.core.api.Assertions.assertThat;

public class ThenRequestWasMadeToStarWarsApi {
  private final TestState testState;
  private int personId;

  public ThenRequestWasMadeToStarWarsApi(TestState testState) {
    this.testState = testState;
  }

  public ThenRequestWasMadeToStarWarsApi forPersonId(int expectedPersonId) {
    this.personId = expectedPersonId;
    return this;
  }

  public void wasSentSuccessfully() {
    HttpRequest actualRequest = testState.getType(REQUEST_FROM_APP_TO_STAR_WARS_SERVICE, HttpRequest.class);
    assertThat(actualRequest.uri().getPath()).contains(String.valueOf(personId));
  }
}
