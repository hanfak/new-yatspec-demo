package adapters.httpclient;

import adapters.thirdparty.AppHttpClient;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Supplier;

import static java.lang.String.format;

public class LoggingHttpClient implements AppHttpClient {

  private final AppHttpClient httpClient;
  private final Logger logger;
  private final Supplier<Timer> timerSupplier;
  private final HttpRequestFormatter requestFormatter;
  private final HttpResponseFormatter httpResponseFormatter;

  public LoggingHttpClient(AppHttpClient httpClient, Logger logger, Supplier<Timer> timerSupplier, HttpRequestFormatter requestFormatter, HttpResponseFormatter httpResponseFormatter) {
    this.httpClient = httpClient;
    this.logger = logger;
    this.timerSupplier = timerSupplier;
    this.requestFormatter = requestFormatter;
    this.httpResponseFormatter = httpResponseFormatter;
  }

  @Override
  public HttpResponse<String> send(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException {
    HttpRequest request = requestBuilder.build();
    String requestUrl = request.uri().toString();
    logger.info(format("Request from App to %s%n%s", requestUrl, requestFormatter.formatRequest(request)));
    Timer timer = timerSupplier.get();
    HttpResponse<String> response = tryToSendRequest(requestBuilder, requestUrl, timer, request);
    Duration elapsedTime = timer.elapsedTime();
    logger.info(format("Response from %s to App received after %dms%n%s", requestUrl, elapsedTime.toMillis(), httpResponseFormatter.formatResponse(response)));
    return response;
  }

  private HttpResponse<String> tryToSendRequest(HttpRequest.Builder requestBuilder, String requestUrl, Timer timer, HttpRequest request) throws IOException, InterruptedException {
    try {
      return httpClient.send(requestBuilder);
    } catch (RuntimeException exception) {
      Duration elapsedTime = timer.elapsedTime();
      logger.error(format("Failed to execute request from App to %s after %dms\n%s", requestUrl, elapsedTime.toMillis(), requestFormatter.formatRequest(request)), exception);
      throw exception;
    }
  }
}
