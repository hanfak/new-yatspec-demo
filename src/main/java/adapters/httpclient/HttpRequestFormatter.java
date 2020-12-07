package adapters.httpclient;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;


public class HttpRequestFormatter {

  public String formatRequest(HttpRequest httpRequest) {
    String method = httpRequest.method();
    String uri = httpRequest.uri().toString();
    String body = getRequestBody(httpRequest);
    String headers = convertHeadersToDisplayFormat(httpRequest.headers());

    return format("%s %s\n%s\n\n%s", method, uri, headers, body);
  }

  private String getRequestBody(HttpRequest httpRequest) {
    Optional<HttpRequest.BodyPublisher> bodyOptional = httpRequest.bodyPublisher();

    if (bodyOptional.isEmpty()) {
      return "";
    }

    HttpRequestBodySubscriber<ByteBuffer> subscriber = new HttpRequestBodySubscriber<>();
    bodyOptional.get().subscribe(subscriber);
    return subscriber.getBodyItems() == null ? "" : new String(subscriber.getBodyItems().array());
  }

  private String convertHeadersToDisplayFormat(HttpHeaders headers) {
    return headers.map().entrySet().stream()
        .flatMap(this::toHeaderAndValuePairStream)
        .collect(joining("\n"));
  }

  private Stream<String> toHeaderAndValuePairStream(Map.Entry<String, List<String>> headerEntry) {
    return headerEntry.getValue()
        .stream()
        .map(headerValue -> format("%s: %s", headerEntry.getKey(), headerValue));
  }
}

