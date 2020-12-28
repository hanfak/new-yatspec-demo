package acceptancetests._03thirdpartyhttpcall.testinfrastructure;

import adapters.outgoing.thirdparty.AppHttpClient;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class starWarsYatspecLoggingClient implements AppHttpClient {
  private final AppHttpClient appHttpClient;

  public starWarsYatspecLoggingClient(AppHttpClient appHttpClient) {
    this.appHttpClient = appHttpClient;
  }

  @Override
  public HttpResponse<String> send(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException {
    return appHttpClient.send(requestBuilder);
  }
}
