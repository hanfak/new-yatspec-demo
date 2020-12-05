package wiring;

import org.slf4j.Logger;
import webserver.JettyWebServer;

import javax.sql.DataSource;

import static settings.PropertyLoader.load;

public final class Application {

  private final ApplicationWiring wiring;

  private JettyWebServer jettyWebServer;

  // For testing
  public Application(ApplicationWiring wiring) {
    this.wiring = wiring;
  }

  Application(String propertyFile, Logger applicationLogger) {
    this(ApplicationWiring.wiring(load(propertyFile), applicationLogger));
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
