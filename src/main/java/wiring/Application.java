package wiring;

import webserver.JettyWebServer;

import javax.sql.DataSource;

import static settings.PropertyLoader.load;

public final class Application {

  private JettyWebServer jettyWebServer;

  private final ApplicationWiring wiring;

  // For testing
  public Application(ApplicationWiring wiring) {
    this.wiring = wiring;
  }

  public Application(String propertyFile) {
    this(ApplicationWiring.wiring(load(propertyFile)));
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
}
