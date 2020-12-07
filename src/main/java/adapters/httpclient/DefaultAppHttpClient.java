package adapters.httpclient;

import adapters.thirdparty.AppHttpClient;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class DefaultAppHttpClient implements AppHttpClient {

  private final HttpClient httpClient;

  public DefaultAppHttpClient() {
    httpClient = HttpClient.newHttpClient();
  }

  @Override
  public HttpResponse<String> send(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException {
    return httpClient.send(requestBuilder.build(), ofString());
  }
}