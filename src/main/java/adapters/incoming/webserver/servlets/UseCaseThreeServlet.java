package adapters.incoming.webserver.servlets;

import core.domain.SpeciesInfo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;

public class UseCaseThreeServlet extends HttpServlet {
  private final DataProvider dataProvider;

  public UseCaseThreeServlet(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int personId = Integer.parseInt(request.getPathInfo().substring(1));

    // Getting data from db
    SpeciesInfo speciesInfo = dataProvider.getSpeciesInfo(personId);
    String birthYear = dataProvider.getBirthYear(personId);
    // Outgoing Response
    response.getWriter().print(format("Hello, %s, who lives for %s years and has average height of %s metres, and was born %s",
        speciesInfo.species,
        speciesInfo.lifeSpan,
        speciesInfo.avgHeight,
        birthYear));
    response.setHeader("Content-Type", "text/html");
    response.setStatus(200);
  }
}
