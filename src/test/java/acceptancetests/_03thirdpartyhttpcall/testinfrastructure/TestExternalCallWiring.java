package acceptancetests._03thirdpartyhttpcall.testinfrastructure;

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
import wiring.ExternalCallWiringInterface;

public class TestExternalCallWiring implements ExternalCallWiringInterface {

  private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger(LoggingCategory.AUDIT.name());

  private final Settings settings;

  private TestExternalCallWiring(Settings settings) {
    this.settings = settings;
  }

  static ExternalCallWiringInterface externalCallWiringFactory(Settings settings) {
    return new TestExternalCallWiring(settings);
  }

  private AppHttpClient appHttpClient() {
    return new LoggingHttpClient(new DefaultAppHttpClient(), AUDIT_LOGGER, Timer::start, new HttpRequestFormatter(), new HttpResponseFormatter());
  }

  private AppHttpClient starWarsYatspecLoggingClient(AppHttpClient appHttpClient) {
    return new starWarsYatspecLoggingClient(appHttpClient);
  }

  @Override
  public StarWarsInterfaceService starWarsInterfaceService() {
    return new StarWarsService(starWarsYatspecLoggingClient(appHttpClient()), settings);
  }

  @Override
  public ActivityService randomXmlService() {
    return new RandomXmlService(appHttpClient(), settings);
  }
}
