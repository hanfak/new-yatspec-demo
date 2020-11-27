package wiring;

import webserver.JettyWebServer;

import javax.sql.DataSource;

import static settings.PropertyLoader.load;

public class Application {

  private JettyWebServer jettyWebServer;

  private final ApplicationWiring wiring;

  // For testing
  public Application(ApplicationWiring wiring) {
    this.wiring = wiring;
  }

  public Application() {
    this(ApplicationWiring.wiring(load("target/classes/application.prod.properties")));
  }

  public static void main(String... args) {
    new Application().start();
  }

  // remove start method
  public void start() {
    this.jettyWebServer = wiring.jettyWebServer();
    this.jettyWebServer.startServer();
  }

  // remove start method
  // For testing
  public void stop() {
    jettyWebServer.stopServer();
  }

  // For testing
  public DataSource getDataSource() {
    return wiring.getDataSource();
  }

//  // Here in case want to reuse
//  private static void setupWebServer(Application application, ApplicationWiring applicationWiring) {
//    ServletContextHandler servletContextHandler = servletHandler().create();
//    ServletBuilder.createServlet(servletContextHandler)
//        .addServlet(applicationWiring.useCaseServlet(), "/usecase/*")
//        .addServlet(applicationWiring.useCaseOneServlet(), "/usecaseone")
//        .addServlet(applicationWiring.useCaseTwoServlet(), "/usecasetwo")
//        .addServlet(applicationWiring.useCaseThreeServlet(), "/usecasethree/*")
//        .addServlet(applicationWiring.useCaseFourServlet(), "/usecasefour/*")
//        .addServlet(applicationWiring.useCaseFiveServlet(), "/usecasefive/*")
//        .addServlet(applicationWiring.useCaseSixServlet(), "/usecasesix/*")
//        .addServlet(applicationWiring.useCaseSevenServlet(), "/usecaseseven/*")
//        .addServlet(applicationWiring.useCaseEightServlet(), "/usecaseeight/*");
//    application.jettyWebServer = jettyWebServer().create(servletContextHandler);
//  }
}
