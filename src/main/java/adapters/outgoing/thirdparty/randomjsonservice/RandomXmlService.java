package adapters.outgoing.thirdparty.randomjsonservice;

import adapters.incoming.webserver.servlets.ActivityService;
import adapters.logging.LoggingCategory;
import adapters.outgoing.httpclient.*;
import adapters.outgoing.thirdparty.AppHttpClient;
import adapters.settings.PropertyLoader;
import adapters.settings.internal.Settings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

import static java.lang.String.format;

public class RandomXmlService implements ActivityService {

  private final static Logger APPLICATION_LOGGER = LoggerFactory.getLogger(LoggingCategory.APPLICATION.name());
  private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger(LoggingCategory.AUDIT.name());

  private final AppHttpClient httpClient;
  private final Settings settings;

  public RandomXmlService(AppHttpClient httpClient, Settings settings) {
    this.httpClient = httpClient;
    this.settings = settings;
  }

  @Override
  public Activity getCharacterInfo(Integer id) throws IOException {
    String apiAddress = settings.randomJsonApiAddress();
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(apiAddress + id))
        .GET()
        .timeout(Duration.ofSeconds(10))
        .header("Accept", "application/json");
    java.net.http.HttpResponse<String> response = tryAndSendRequest(requestBuilder);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(response.body(), Activity.class);
  }

  // Local tester to remove
  public static void main(String... args) throws IOException {
    RandomXmlService randomXmlService = new RandomXmlService(new LoggingHttpClient(new DefaultAppHttpClient(), AUDIT_LOGGER, Timer::start, new HttpRequestFormatter(), new HttpResponseFormatter()), PropertyLoader.load("target/classes/application.prod.properties"));
    randomXmlService.getCharacterInfo(1);
  }

  private java.net.http.HttpResponse<String> tryAndSendRequest(HttpRequest.Builder requestBuilder) {
    try {
      return httpClient.send(requestBuilder);
    } catch (Exception e) {
      APPLICATION_LOGGER.error(format("Unexpected exception when getting data from api due to '%s'", e.getMessage()), e);
      throw new IllegalStateException(e);
    }
  }

  // TODO do a post
  // Go to xml api https://fakerestapi.azurewebsites.net/swagger/ui/index#!/Activities/Activities_Get

}
