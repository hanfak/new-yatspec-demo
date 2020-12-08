package adapters.incoming.webserver.servlets.jmsexample;

import core.usecases.services.jmsexample.UseCaseExampleOneStepOne;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JmsExampleOneServlet extends HttpServlet {

  private final UseCaseExampleOneStepOne useCaseExampleOneStepOne;

  public JmsExampleOneServlet(UseCaseExampleOneStepOne useCaseExampleOneStepOne) {
    this.useCaseExampleOneStepOne = useCaseExampleOneStepOne;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    useCaseExampleOneStepOne.execute();

    // TODO delegate for marshalling json
    resp.getWriter().print("In progress");
  }
}
