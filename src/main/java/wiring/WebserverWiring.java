package wiring;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import webserver.JettyWebServer;

import static webserver.factories.JettyWebServerFactory.jettyWebServer;
import static webserver.factories.ServletBuilder.createServlet;

public class WebserverWiring {

  private final Logger applicationLogger;

  public WebserverWiring(Logger applicationLogger) {
    this.applicationLogger = applicationLogger;
  }

  public static WebserverWiring webserverWiring(Logger applicationLogger) {
    return new WebserverWiring(applicationLogger);
  }

  JettyWebServer setupWebServer(ApplicationWiring applicationWiring) {
    ServletContextHandler servletContextHandler = new ServletContextHandler();
    createServlet(servletContextHandler)
        .addServlet(applicationWiring.useCaseServlet(), "/usecase/*")
        .addServlet(applicationWiring.useCaseOneServlet(), "/usecaseone")
        .addServlet(applicationWiring.useCaseTwoServlet(), "/usecasetwo")
        .addServlet(applicationWiring.useCaseThreeServlet(), "/usecasethree/*")
        .addServlet(applicationWiring.useCaseFourServlet(), "/usecasefour/*")
        .addServlet(applicationWiring.useCaseFiveServlet(), "/usecasefive/*")
        .addServlet(applicationWiring.useCaseSixServlet(), "/usecasesix/*")
        .addServlet(applicationWiring.useCaseSevenServlet(), "/usecaseseven/*")
        .addServlet(applicationWiring.useCaseEightServlet(), "/usecaseeight/*")
        .addServlet(applicationWiring.generateResponseLetterUseCase(), "/generateResponseLetter");

    return jettyWebServer(applicationLogger).create(servletContextHandler);
  }
}
