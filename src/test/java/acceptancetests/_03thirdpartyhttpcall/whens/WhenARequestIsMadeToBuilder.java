package acceptancetests._03thirdpartyhttpcall.whens;

import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static acceptancetests._03thirdpartyhttpcall.testinfrastructure.YatspecConstants.REQUEST_FROM_CLIENT_TO_APP;
import static acceptancetests._03thirdpartyhttpcall.testinfrastructure.YatspecConstants.RESPONSE_FROM_APP_TO_CLIENT;

public class WhenARequestIsMadeToBuilder {
  private final TestState testState;
  private final HttpClient httpClient;

  private String uri = "undefined";
  private HttpResponse<String> httpResponse;
  private final List<String> headers = new ArrayList<>();

  public WhenARequestIsMadeToBuilder(TestState testState) {
    this.testState = testState;
    this.httpClient = HttpClient.newHttpClient();
  }

  public HttpResponse<String> getHttpResponse() {
    return httpResponse;
  }

  public WhenARequestIsMadeToBuilder withUri(String uri) {
    this.uri = uri;
    return this;
  }

  public WhenARequestIsMadeToBuilder withHeader(String key, String value) {
    this.headers.add(key);
    this.headers.add(value);
    return this;
  }

  public void isCalledUsingHttpGetMethod() throws IOException, InterruptedException {
    HttpRequest httpRequest = createGetRequest().build();
    testState.log(REQUEST_FROM_CLIENT_TO_APP, httpRequest);
    httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    testState.log(RESPONSE_FROM_APP_TO_CLIENT, httpResponse);
  }

  private HttpRequest.Builder createGetRequest() {
    String[] headers = this.headers.toArray(String[]::new);
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
        .GET() // This can be extracted as builder method
        .uri(URI.create(this.uri));
    if (headers.length > 0) {
      return requestBuilder.headers(headers);
    }
    return requestBuilder;
  }
}
