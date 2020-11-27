package webserver.factories;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;

public class ServletBuilder {

  private final ServletContextHandler servletContextHandler;

  private ServletBuilder(ServletContextHandler servletContextHandler) {
    this.servletContextHandler = servletContextHandler;
  }

  public static ServletBuilder createServlet(ServletContextHandler servletContextHandler) {
    return new ServletBuilder(servletContextHandler);
  }

  public ServletBuilder addServlet(HttpServlet httpServlet, String path) {
    servletContextHandler.addServlet(new ServletHolder(httpServlet), path);
    return this;
  }
}
