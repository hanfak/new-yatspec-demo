package acceptancetests._03thirdpartyhttpcall.testinfrastructure;

import adapters.outgoing.thirdparty.AppHttpClient;
import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static acceptancetests._03thirdpartyhttpcall.testinfrastructure.YatspecConstants.REQUEST_FROM_APP_TO_STAR_WARS_SERVICE;
import static acceptancetests._03thirdpartyhttpcall.testinfrastructure.YatspecConstants.RESPONSE_FROM_APP_TO_STAR_WARS_SERVICE;

public class StarWarsYatspecLoggingClient implements AppHttpClient {
  private final AppHttpClient appHttpClient;
  private final TestState testState;

  public StarWarsYatspecLoggingClient(AppHttpClient appHttpClient, TestState testState) {
    this.appHttpClient = appHttpClient;
    this.testState = testState;
  }

  @Override
  public HttpResponse<String> send(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException {
    testState.log(REQUEST_FROM_APP_TO_STAR_WARS_SERVICE, requestBuilder.build());
    HttpResponse<String> response = appHttpClient.send(requestBuilder);
    testState.log(RESPONSE_FROM_APP_TO_STAR_WARS_SERVICE, response);
    return response;
  }
}

