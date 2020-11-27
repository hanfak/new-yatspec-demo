package thirdparty.starwarsservice;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import domain.Person;
import domain.Species;
import logging.LoggingCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.Settings;
import thirdparty.AppHttpClient;
import webserver.servlets.StarWarsInterfaceService;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

import static java.lang.String.format;

public class StarWarsService implements StarWarsInterfaceService {

  private final static Logger APPLICATION_LOGGER = LoggerFactory.getLogger(LoggingCategory.APPLICATION.name());

  private final AppHttpClient httpClient;
  private final Settings settings;

  public StarWarsService(AppHttpClient httpClient, Settings settings) {
    this.httpClient = httpClient;
    this.settings = settings;
  }

  @Override
  public Person getCharacterInfo(String id) {
    String apiAddress = settings.starWarsApiAddress();
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(apiAddress + "people/" + id + "/"))
        .GET()
        .timeout(Duration.ofSeconds(10));
    java.net.http.HttpResponse<String> response = tryAndSendRequest(requestBuilder);
    // TODO sad path, if not success response received
    String body = response.body();
    System.out.println("body = " + body);
    DocumentContext personJson = JsonPath.parse(body);
    String species = personJson.read("$.species[0]");
    String name = personJson.read("$.name");
    String birthYear = personJson.read("$.birth_year");
    return new Person(species, name, birthYear); // change
  }

  @Override
  public Species getSpeciesInfo(String url) {
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url))
        .GET()
        .timeout(Duration.ofSeconds(10));
    java.net.http.HttpResponse<String> response = tryAndSendRequest(requestBuilder);
    // TODO sad path, if not success response received
    String body = response.body();
    DocumentContext speciesJson = JsonPath.parse(body);

    String name = speciesJson.read("$.name");
    Integer lifeSpan = Integer.parseInt(speciesJson.read("$.average_lifespan"));
    Double averageHeight = Double.parseDouble(speciesJson.read("$.average_height"));
    return new Species(name, lifeSpan, averageHeight);
  }

  private java.net.http.HttpResponse<String> tryAndSendRequest(HttpRequest.Builder requestBuilder) {
    try {
      return httpClient.send(requestBuilder);
    } catch (Exception e) {
      APPLICATION_LOGGER.error(format("Unexpected exception when getting data from api due to '%s'", e.getMessage()), e);
      throw new IllegalStateException(e);
    }
  }
}
