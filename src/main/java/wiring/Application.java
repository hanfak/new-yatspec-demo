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

  public void start() {
    jettyWebServer = wiring.jettyWebServer();
    wiring.setupJmsListeners();
    jettyWebServer.startServer();
    wiring.startJmsListeners();
  }

  // For testing
  public void stop() {
    wiring.stopJmsListeners();
    jettyWebServer.stopServer();
    // TODO: close datasource here
  }

  // For testing
  public DataSource getDataSource() {
    return wiring.getDataSource();
  }
}
