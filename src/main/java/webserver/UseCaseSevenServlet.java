package webserver;

import domain.Person;
import domain.Species;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class UseCaseSevenServlet extends HttpServlet {

  private final StarWarsInterfaceService starWarsInterfaceService;
  private final DataProvider dataProvider;

  public UseCaseSevenServlet(StarWarsInterfaceService starWarsInterfaceService, DataProvider dataProvider) {
    this.starWarsInterfaceService = starWarsInterfaceService;
    this.dataProvider = dataProvider;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String personName = request.getPathInfo().substring(1);
    String personId = String.valueOf(dataProvider.getPersonId(personName));

    // Go to third party app get data
    Person characterInfo = starWarsInterfaceService.getCharacterInfo(personId);
    Species speciesInfo = starWarsInterfaceService.getSpeciesInfo(characterInfo.getSpecies());

    response.getWriter().print(format("{\"Description\": \"%s was born on %s and is %s species\"}",
        characterInfo.getName(), characterInfo.getBirthYear(), speciesInfo.getName()));
    response.setHeader("Content-Type", "application/json");
    response.setStatus(200);
  }
}
