package webserver;

import databaseservice.DataProvider;
import domain.Activity;
import domain.Person;
import domain.Species;
import thirdparty.randomjsonservice.RandomXmlService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class UseCaseEightServlet extends HttpServlet {

  private final StarWarsInterfaceService starWarsService;
  private final RandomXmlService randomXmlService;
  private final DataProvider dataProvider;

  public UseCaseEightServlet(StarWarsInterfaceService starWarsService, RandomXmlService randomXmlService, DataProvider dataProvider) {
    this.starWarsService = starWarsService;
    this.randomXmlService = randomXmlService;
    this.dataProvider = dataProvider;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String personName = request.getPathInfo().substring(1);
    Integer personIdInt = dataProvider.getPersonId(personName);
    String personId = String.valueOf(personIdInt);

    // Go to third party app get data
    Person characterInfo = starWarsService.getCharacterInfo(personId);
    Species speciesInfo = starWarsService.getSpeciesInfo(characterInfo.getSpecies());
    Activity activity = randomXmlService.getCharacterInfo(personIdInt);
    response.getWriter().print(format("{\"Description\": \"%s was born on %s and is %s species and has performed %s and has status: %s at %s\"}",
            characterInfo.getName(), characterInfo.getBirthYear(), speciesInfo.getName(),
            activity.getTitle(), activity.getCompleted(), activity.getDueDate()));
    response.setHeader("Content-Type", "application/json");
    response.setStatus(200);
  }
}
