package wiring;

import org.eclipse.jetty.servlet.ServletContextHandler;
import webserver.JettyWebServer;

import static webserver.factories.JettyWebServerFactory.jettyWebServer;
import static webserver.factories.ServletBuilder.createServlet;

public class WebserverWiring {

  private final JettyWebServer jettyWebServer;

  private WebserverWiring() {
    throw new AssertionError("Should not be instantiated outside of static factory method");
  }

  private WebserverWiring(JettyWebServer jettyWebServer) {
    this.jettyWebServer = jettyWebServer;
  }

  static WebserverWiring webserverWiring(JettyWebServer jettyWebServer) {
    return new WebserverWiring(jettyWebServer);
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
        .addServlet(applicationWiring.generateResponseLetterUseCaseServlet(), "/generateResponseLetter")
        .addServlet(applicationWiring.jmsExampleOneServlet(), "/jmsExampleOneServlet");

    return jettyWebServer(jettyWebServer).create(servletContextHandler);
  }
}
