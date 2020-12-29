package acceptancetests._03thirdpartyhttpcall.testinfrastructure;

import acceptancetests._03thirdpartyhttpcall.testinfrastructure.renderers.CustomJavaSourceRenderer;
import acceptancetests._03thirdpartyhttpcall.testinfrastructure.renderers.HttpRequestRenderer;
import acceptancetests._03thirdpartyhttpcall.testinfrastructure.renderers.HttpResponseRenderer;
import acceptancetests._03thirdpartyhttpcall.thens.ThenTheResponse;
import acceptancetests._03thirdpartyhttpcall.thens.ThenTheResponseVersion2;
import acceptancetests._03thirdpartyhttpcall.whens.WhenARequestIsMadeTo;
import acceptancetests._03thirdpartyhttpcall.whens.WhenARequestIsMadeToBuilder;
import adapters.logging.LoggingCategory;
import adapters.settings.internal.Settings;
import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
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

  public final TestState testState = new TestState();

  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  private static final Settings settings = load(TEST_PROPERTIES_PATH);
  private static final DataRepositoryFactory dataRepositoryFactory = new DataRepositoryFactory(createDataSource(settings), APPLICATION_LOGGER);
  private static final Application application = new Application(wiringWithCustomAdapters(settings, APPLICATION_LOGGER, dataRepositoryFactory, Optional.of(externalCallWiringFactory(settings)), empty(), empty()));

  public final WhenARequestIsMadeTo whenARequestIsMadeTo = new WhenARequestIsMadeTo(testState);
  public final ThenTheResponse thenTheResponse = new ThenTheResponse(whenARequestIsMadeTo::getHttpResponse);

  public final WhenARequestIsMadeToBuilder whenARequest = new WhenARequestIsMadeToBuilder(testState);
  public final ThenTheResponse thenResponse = new ThenTheResponse(whenARequest::getHttpResponse);
  public final ThenTheResponseVersion2 thenReturnedResponse = new ThenTheResponseVersion2(whenARequest::getHttpResponse);


  @Override
  public Collection<SpecResultListener> getResultListeners() throws Exception {
    return List.of(
        new HtmlResultRenderer()
            .withCustomRenderer(HttpRequest.class, new HttpRequestRenderer())
            .withCustomRenderer(HttpResponse.class, new HttpResponseRenderer())
            .withCustomRenderer(JavaSource.class, new CustomJavaSourceRenderer())
            .withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer<>()),
        new HtmlIndexRenderer()
    );
  }

  @BeforeEach
  void setUp() {
    testState.reset();
    // Instead of starting application each time during build, can use docker image
    // created before tests are run during build, and the tests are run against this. Will need some logic
    // To check if container is running, then dont start app locally
    application.start();
  }

  @AfterEach
  void tearDown() {
    application.stop();
  }
}
