package wiring;

import adapters.incoming.webserver.servlets.ActivityService;
import adapters.incoming.webserver.servlets.StarWarsInterfaceService;
import adapters.logging.LoggingCategory;
import adapters.outgoing.httpclient.*;
import adapters.outgoing.thirdparty.AppHttpClient;
import adapters.outgoing.thirdparty.randomjsonservice.RandomXmlService;
import adapters.outgoing.thirdparty.starwarsservice.StarWarsService;
import adapters.settings.internal.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalCallWiring {

  private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger(LoggingCategory.AUDIT.name());

  private final Settings settings;

  private ExternalCallWiring(Settings settings) {
    this.settings = settings;
  }

  static ExternalCallWiring externalCallWiringFactory(Settings settings) {
    return new ExternalCallWiring(settings);
  }

  private AppHttpClient appHttpClient() {
    return new LoggingHttpClient(new DefaultAppHttpClient(), AUDIT_LOGGER, Timer::start, new HttpRequestFormatter(), new HttpResponseFormatter());
  }

  StarWarsInterfaceService starWarsInterfaceService() {
    return new StarWarsService(appHttpClient(), settings);
  }

  ActivityService randomXmlService() {
    return new RandomXmlService(appHttpClient(), settings);
  }
}
