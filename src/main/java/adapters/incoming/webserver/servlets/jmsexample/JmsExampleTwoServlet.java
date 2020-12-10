package adapters.incoming.webserver.servlets.jmsexample;

import core.usecases.services.jmsexample.exampletwo.UseCaseExampleTwoStepOne;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JmsExampleTwoServlet  extends HttpServlet  {

  private final UseCaseExampleTwoStepOne useCaseExampleTwoStepOne;

  public JmsExampleTwoServlet(UseCaseExampleTwoStepOne useCaseExampleTwoStepOne) {
    this.useCaseExampleTwoStepOne = useCaseExampleTwoStepOne;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    useCaseExampleTwoStepOne.execute();

    resp.getWriter().print("In progress");
  }
}
