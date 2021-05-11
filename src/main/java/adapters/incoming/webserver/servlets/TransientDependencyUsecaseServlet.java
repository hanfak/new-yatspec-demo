package adapters.incoming.webserver.servlets;

import core.usecases.TransientDependencyUsecase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TransientDependencyUsecaseServlet extends HttpServlet {

  private final TransientDependencyUsecase transientDependencyUsecase;

  public TransientDependencyUsecaseServlet(TransientDependencyUsecase transientDependencyUsecase) {
    this.transientDependencyUsecase = transientDependencyUsecase;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.getWriter().print(transientDependencyUsecase.execute());
  }
}
