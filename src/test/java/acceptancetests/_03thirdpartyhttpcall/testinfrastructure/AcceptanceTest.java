package acceptancetests._03thirdpartyhttpcall.testinfrastructure;

import acceptancetests._03thirdpartyhttpcall.givens.GivenStarWarsCharacterInformationService;
import acceptancetests._03thirdpartyhttpcall.thens.ThenRequestWasMadeToStarWarsApi;
import acceptancetests._03thirdpartyhttpcall.whens.WhenARequestIsMadeToBuilder;
import adapters.logging.LoggingCategory;
import adapters.settings.internal.Settings;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import testinfrastructure.YatspecLoggerCapturer;
import testinfrastructure.renderers.CustomJavaSourceRenderer;
import testinfrastructure.renderers.HttpRequestRenderer;
import testinfrastructure.renderers.HttpResponseRenderer;
import wiring.Application;
import wiring.DataRepositoryFactory;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static acceptancetests._03thirdpartyhttpcall.testinfrastructure.TestExternalCallWiring.externalCallWiringFactory;
import static adapters.outgoing.databaseservice.DatasourceConfig.createDataSource;
import static adapters.settings.PropertyLoader.load;
import static java.util.Optional.empty;
import static org.slf4j.LoggerFactory.getLogger;
import static wiring.ApplicationWiring.wiringWithCustomAdapters;

@ExtendWith(SequenceDiagramExtension.class)
public class AcceptanceTest implements WithCustomResultListeners {
  private static final String TEST_PROPERTIES_PATH = "target/test-classes/application.test.properties";

  public static final TestState testState = new TestState();

  private static final Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());
  private static final Settings settings = load(TEST_PROPERTIES_PATH);
  private static final DataRepositoryFactory dataRepositoryFactory = new DataRepositoryFactory(createDataSource(settings), APPLICATION_LOGGER);
  private static final Application application = new Application(wiringWithCustomAdapters(settings, APPLICATION_LOGGER, dataRepositoryFactory, Optional.of(externalCallWiringFactory(settings, testState)), empty(), empty()));

  protected static final WireMockServer wiremock = new WireMockServer(8090);

  public final GivenStarWarsCharacterInformationService givenStarWarsCharacterInformationService = new GivenStarWarsCharacterInformationService(testState, settings);
  public final WhenARequestIsMadeToBuilder whenARequest = new WhenARequestIsMadeToBuilder(testState);
  public final ThenRequestWasMadeToStarWarsApi thenRequestWasMadeToStarWarsApi = new ThenRequestWasMadeToStarWarsApi(testState);

  private final YatspecLoggerCapturer yatspecLoggerCapture = new YatspecLoggerCapturer(testState);

  @Override
  public Collection<SpecResultListener> getResultListeners() throws Exception {
    return List.of(
        new HtmlResultRenderer()
            .withCustomRenderer(HttpRequest.class,result -> new HttpRequestRenderer())
            .withCustomRenderer(HttpResponse.class,result -> new HttpResponseRenderer())
            .withCustomRenderer(JavaSource.class,result -> new CustomJavaSourceRenderer())
            .withCustomRenderer(SvgWrapper.class,result -> new DontHighlightRenderer<>()),
        new HtmlIndexRenderer()
    );
  }

  @BeforeAll
  static void  beforeAll() {
    testState.reset();
    wiremock.start();
    // Instead of starting application each time during build, can use docker image
    // created before tests are run during build, and the tests are run against this. Will need some logic
    // To check if container is running, then dont start app locally
    application.start();
  }

  @BeforeEach
  void setUp() {
    yatspecLoggerCapture.beginCapturingOutput();
    WireMock.configureFor(8090);
    wiremock.resetAll(); // each test will setup own responses in givens, might conflict with other tests
  }

  @AfterEach
  void tearDown() {
    yatspecLoggerCapture.captureOutputToYatspec();
  }

  @AfterAll
  static void AfterAll() {
    application.stop();
    wiremock.stop();
  }
}
