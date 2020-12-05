package webserver.servlets.jmsexample;

import usecases.UseCaseStepOne;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JmsExampleOneServlet extends HttpServlet {

  private final UseCaseStepOne useCaseStepOne;

  public JmsExampleOneServlet(UseCaseStepOne useCaseStepOne) {
    this.useCaseStepOne = useCaseStepOne;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    useCaseStepOne.execute();

    // TODO delegate for marshalling json
    resp.getWriter().print("In progress");
  }
}
