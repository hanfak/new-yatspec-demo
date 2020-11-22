package acceptancetests._01reqandresponly.whens;

import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static acceptancetests._01reqandresponly.testinfrastructure.AcceptanceTest.REQUEST_FROM_CLIENT_TO_APP;
import static acceptancetests._01reqandresponly.testinfrastructure.AcceptanceTest.RESPONSE_FROM_APP_TO_CLIENT;
import static java.net.http.HttpRequest.BodyPublishers.ofString;

public class WhenARequestIsMadeTo {

  private static final String BASE_URI = "http://localhost:2222/";
  private final TestState testState;
  private String url = "undefined";
  private final HttpClient httpClient;
  private HttpResponse<String> httpResponse;

  public WhenARequestIsMadeTo(TestState testState) {
    this.testState = testState;
    httpClient = HttpClient.newHttpClient();
  }

  public HttpResponse<String> getHttpResponse() {
    return httpResponse;
  }

  public void theUseCaseOne() throws IOException, InterruptedException {
    url = BASE_URI + "usecaseone";
    getRequestWithNoBody();
  }

  public void theUseCaseOneWithUri(String url) throws IOException, InterruptedException {
    this.url = url;
    getRequestWithNoBody();
  }


  private void getRequestWithNoBody() throws IOException, InterruptedException {
    HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();
    testState.log(REQUEST_FROM_CLIENT_TO_APP, httpRequest);
    httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    testState.log(RESPONSE_FROM_APP_TO_CLIENT, httpResponse);
  }
  // Can add multiple different requests (PUT, PATCH etc)
  // Can template some of the request

  private void postRequestWithBody(String body) throws IOException, InterruptedException {
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .POST(ofString(body))
        .header("Content-Type", "application/json")
        .uri(URI.create(url))
        .build();
    testState.log(REQUEST_FROM_CLIENT_TO_APP, httpRequest);
    httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    testState.log(RESPONSE_FROM_APP_TO_CLIENT, httpResponse);
  }

  public void anEndpointThatBlowsUp() throws IOException, InterruptedException {
    url = BASE_URI + "badEndpoint";
    getRequestWithNoBody();
  }

  public void anEndpointThatDoesNotExist() throws IOException, InterruptedException {
    url = BASE_URI + "notFound";
    getRequestWithNoBody();
  }
}
