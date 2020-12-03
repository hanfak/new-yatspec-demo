package wiring;

import org.eclipse.jetty.servlet.ServletContextHandler;
import webserver.JettyWebServer;

import static webserver.factories.JettyWebServerFactory.jettyWebServer;
import static webserver.factories.ServletBuilder.createServlet;
import static wiring.ApplicationWiring.APPLICATION_LOGGER;

public class WebserverWiring {

  public static WebserverWiring webserverWiring() {
    return new WebserverWiring();
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

    return jettyWebServer(APPLICATION_LOGGER).create(servletContextHandler);
  }
}
