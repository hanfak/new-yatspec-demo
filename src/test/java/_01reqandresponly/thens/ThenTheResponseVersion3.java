package _01reqandresponly.thens;

import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenTheResponseVersion3 {
  private final TestState testState;
  private final HttpResponse<String> response;

  public ThenTheResponseVersion3(TestState testState, HttpResponse<String> response) {
    this.testState = testState;
    this.response = response;
  }

  public ThenTheResponseVersion3 hasStatusCode(int expectedStatusCode) {
    assertThat(response.statusCode()).isEqualTo(expectedStatusCode);
    return this;
  }

  public ThenTheResponseVersion3 hasContentType(String expectedContentType) {
    assertThat(response.headers().firstValue("content-type")).contains(expectedContentType);
    return this;
  }

  public ThenTheResponseVersion3 hasBody(String expectedBody) {
    assertThat(response.body()).isEqualTo(expectedBody);
    return this;
  }
}
