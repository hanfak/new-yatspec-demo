package adapters.incoming.webserver.servlets.exteranlcalls;

import adapters.incoming.webserver.servlets.Marshaller;
import adapters.incoming.webserver.servlets.exteranlcalls.ExternalCallExampleOutgoingDTO.Details;
import core.usecases.ports.incoming.ExternalCallExampleOneService;
import core.usecases.ports.incoming.ExternalCallExampleOneService.Output;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExternalCallExampleOneServlet extends HttpServlet {

  private final ExternalCallExampleOneService usecase;
  private final Marshaller<ExternalCallExampleOutgoingDTO, String> marshaller;

  public ExternalCallExampleOneServlet(ExternalCallExampleOneService usecase, Marshaller<ExternalCallExampleOutgoingDTO, String> marshaller) {
    this.usecase = usecase;
    this.marshaller = marshaller;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String personId = request.getPathInfo().substring(1);

    Output output = usecase.execute(personId);

    String jsonBody = marshaller.marshall(new ExternalCallExampleOutgoingDTO(personId, new Details(output.getName(), output.getBirthYear())));

    createResponse(response, jsonBody);
  }

  private void createResponse(HttpServletResponse response, String jsonBody) throws IOException {
    response.getWriter().print(jsonBody);
    response.setHeader("Content-Type", "application/json");
    response.setStatus(200);
  }
}
