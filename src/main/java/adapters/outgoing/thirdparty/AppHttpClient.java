package adapters.outgoing.thirdparty;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface AppHttpClient {
  HttpResponse<String> send(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException;
}
