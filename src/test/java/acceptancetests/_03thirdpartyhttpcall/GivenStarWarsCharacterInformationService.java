package acceptancetests._03thirdpartyhttpcall;

import adapters.settings.internal.Settings;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.googlecode.yatspec.state.givenwhenthen.TestState;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static java.lang.String.format;

public class GivenStarWarsCharacterInformationService {

  private final TestState testState;
  private final Settings settings;

  private int personId;
  private String birthYear;
  private String name;

  public GivenStarWarsCharacterInformationService(TestState testState, Settings settings) {
    this.testState = testState;
    this.settings = settings;
  }

  public GivenStarWarsCharacterInformationService forPersonId(int expectedPersonId) {
    this.personId = expectedPersonId;
    return this;
  }

  public GivenStarWarsCharacterInformationService withBirthYear(String expectedBirthYear) {
    this.birthYear = expectedBirthYear;
    return this;
  }

  public GivenStarWarsCharacterInformationService withName(String expectedName) {
    this.name = expectedName;
    return this;
  }

  public void willRespondWithSuccessfully() {
    String path = URI.create(settings.starWarsApiAddress()).getPath();
    String url = path + "people/" + personId + "/";
    ResponseDefinitionBuilder primedResponse = aResponse()
        .withStatus(200)
        .withHeader("content-type", "application/json")
        .withBody(format(RESPONSE_TEMPLATE, name, birthYear, personId));
    stubFor(get(urlPathEqualTo(url)).willReturn(primedResponse));
  }

  // TODO https://stackoverflow.com/questions/2286648/named-placeholders-in-string-formatting
  // Due to complex json, if need to prime more fields, can use a map to store all the fields to sub in
  private static final String RESPONSE_TEMPLATE = "" +
      "{\n" +
      "  \"name\": \"%s\",\n" +
      "  \"height\": \"228\",\n" +
      "  \"mass\": \"112\",\n" +
      "  \"hair_color\": \"brown\",\n" +
      "  \"skin_color\": \"unknown\",\n" +
      "  \"eye_color\": \"blue\",\n" +
      "  \"birth_year\": \"%s\",\n" +
      "  \"gender\": \"male\",\n" +
      "  \"homeworld\": \"https://swapi.py4e.com/api/planets/14/\",\n" +
      "  \"films\": [\n" +
      "    \"https://swapi.py4e.com/api/films/1/\",\n" +
      "    \"https://swapi.py4e.com/api/films/2/\",\n" +
      "    \"https://swapi.py4e.com/api/films/3/\",\n" +
      "    \"https://swapi.py4e.com/api/films/6/\",\n" +
      "    \"https://swapi.py4e.com/api/films/7/\"\n" +
      "  ],\n" +
      "  \"species\": [\n" +
      "    \"https://swapi.py4e.com/api/species/3/\"\n" +
      "  ],\n" +
      "  \"vehicles\": [\n" +
      "    \"https://swapi.py4e.com/api/vehicles/19/\"\n" +
      "  ],\n" +
      "  \"starships\": [\n" +
      "    \"https://swapi.py4e.com/api/starships/10/\",\n" +
      "    \"https://swapi.py4e.com/api/starships/22/\"\n" +
      "  ],\n" +
      "  \"created\": \"2014-12-10T16:42:45.066000Z\",\n" +
      "  \"edited\": \"2014-12-20T21:17:50.332000Z\",\n" +
      "  \"url\": \"https://swapi.py4e.com/api/people/%s/\"\n" +
      "}";
}
