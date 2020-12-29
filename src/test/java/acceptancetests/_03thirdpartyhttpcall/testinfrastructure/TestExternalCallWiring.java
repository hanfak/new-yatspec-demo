package acceptancetests._03thirdpartyhttpcall.testinfrastructure;

import adapters.incoming.webserver.servlets.ActivityService;
import adapters.incoming.webserver.servlets.StarWarsInterfaceService;
import adapters.logging.LoggingCategory;
import adapters.outgoing.httpclient.*;
import adapters.outgoing.thirdparty.AppHttpClient;
import adapters.outgoing.thirdparty.randomjsonservice.RandomXmlService;
import adapters.outgoing.thirdparty.starwarsservice.StarWarsService;
import adapters.settings.internal.Settings;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wiring.ExternalCallWiringInterface;

public class TestExternalCallWiring implements ExternalCallWiringInterface {

  private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger(LoggingCategory.AUDIT.name());

  private final Settings settings;
  private final TestState testState;

  private TestExternalCallWiring(Settings settings, TestState testState) {
    this.settings = settings;
    this.testState = testState;
  }

  static ExternalCallWiringInterface externalCallWiringFactory(Settings settings, TestState testState) {
    return new TestExternalCallWiring(settings, testState);
  }

  private AppHttpClient appHttpClient() {
    return new LoggingHttpClient(new DefaultAppHttpClient(), AUDIT_LOGGER, Timer::start, new HttpRequestFormatter(), new HttpResponseFormatter());
  }

  private AppHttpClient starWarsYatspecLoggingClient(AppHttpClient appHttpClient) {
    return new StarWarsYatspecLoggingClient(appHttpClient, testState);
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
