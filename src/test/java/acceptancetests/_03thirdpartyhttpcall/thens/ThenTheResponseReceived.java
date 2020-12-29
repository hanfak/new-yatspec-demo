package acceptancetests._03thirdpartyhttpcall.thens;

import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenTheResponseReceived {
  private final TestState testState;
  private final HttpResponse<String> response;

  public ThenTheResponseReceived(TestState testState, HttpResponse<String> response) {
    this.testState = testState;
    this.response = response;
  }

  public ThenTheResponseReceived hasStatusCode(int expectedStatusCode) {
    assertThat(response.statusCode()).isEqualTo(expectedStatusCode);
    return this;
  }

  public ThenTheResponseReceived hasContentType(String expectedContentType) {
    assertThat(response.headers().firstValue("content-type")).contains(expectedContentType);
    return this;
  }

  public ThenTheResponseReceived hasBody(String expectedBody) {
    assertThat(response.body()).isEqualTo(expectedBody);
    return this;
  }
}
