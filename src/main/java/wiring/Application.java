package wiring;

import jmsservice.listener.ApplicationMessageListener;
import jmsservice.listener.configuration.ConfigurableDefaultMessageListenerContainer;
import org.slf4j.Logger;
import webserver.JettyWebServer;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static settings.PropertyLoader.load;

public final class Application {

  private final List<ApplicationMessageListener> applicationMessageListeners = new ArrayList<>();

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
    addConsumerConfiguration();
    this.jettyWebServer.startServer();
    applicationMessageListeners
        .forEach(ApplicationMessageListener::start);
  }

  // remove start method
  // For testing
  public void stop() {
    applicationMessageListeners
        .forEach(ApplicationMessageListener::stop);
    jettyWebServer.stopServer();
    // TODO: close datasource here
  }

  // For testing
  public DataSource getDataSource() {
    return wiring.getDataSource();
  }

  // TODO move to wiring
  private void addConsumerConfiguration() {
    applicationMessageListeners.clear();
    ConfigurableDefaultMessageListenerContainer defaultMessageListenerContainer = new ConfigurableDefaultMessageListenerContainer(wiring.activeMQConnectionFactory());
    applicationMessageListeners.add(new ApplicationMessageListener(wiring.UseCaseExampleOneStepTwoInstructionListener(), defaultMessageListenerContainer));
  }
}
