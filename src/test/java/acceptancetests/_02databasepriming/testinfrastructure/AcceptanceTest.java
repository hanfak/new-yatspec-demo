package acceptancetests._02databasepriming.testinfrastructure;

import acceptancetests._02databasepriming.givens.*;
import acceptancetests._02databasepriming.thens.ThenTheCharacterInfoDatabaseContains;
import acceptancetests._02databasepriming.thens.ThenTheDatabaseContains;
import acceptancetests._02databasepriming.thens.ThenTheResponseVersion2;
import acceptancetests._02databasepriming.whens.WhenARequestIsMadeToBuilder;
import adapters.logging.LoggingCategory;
import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import testinfrastructure.renderers.CustomJavaSourceRenderer;
import testinfrastructure.renderers.HttpRequestRenderer;
import testinfrastructure.renderers.HttpResponseRenderer;
import wiring.Application;
import wiring.ApplicationWiring;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

import static adapters.settings.PropertyLoader.load;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(SequenceDiagramExtension.class)
public class AcceptanceTest implements WithCustomResultListeners {

  private static final String TEST_PROPERTIES_PATH = "target/test-classes/application.test.properties";

  public final TestState testState = new TestState();

  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  private static  Application application = new Application(ApplicationWiring.wiring(load(TEST_PROPERTIES_PATH), APPLICATION_LOGGER));
  // Give control of what db to use in test, will need to configure pom.xml
  private final DSLContext dslContext = DSL.using(application.getDataSource(), SQLDialect.POSTGRES);
  private final TestDataProvider testDataProvider = new TestDataProvider(dslContext);

  public final GivenTheDatabaseContainsVersion1 givenTheDatabaseContainsVersion1 = new GivenTheDatabaseContainsVersion1(dslContext);
  public final GivenTheDatabaseContainsVersion2 givenTheDatabaseContainsVersion2 = new GivenTheDatabaseContainsVersion2(testState, dslContext);
  public final GivenTheDatabaseContainsVersion3 givenTheDatabaseContainsVersion3 = new GivenTheDatabaseContainsVersion3(testState, dslContext);
  public final GivenTheDatabaseContainsVersion4 givenTheDatabaseContainsVersion4 = new GivenTheDatabaseContainsVersion4(testState, dslContext);
  public final GivenTheDatabaseContainsVersion5 givenTheDatabaseContainsVersion5 = new GivenTheDatabaseContainsVersion5(testState, testDataProvider);

  public final WhenARequestIsMadeToBuilder whenARequest = new WhenARequestIsMadeToBuilder(testState);

  public final ThenTheResponseVersion2 thenReturnedResponse = new ThenTheResponseVersion2(whenARequest::getHttpResponse);
  public final ThenTheDatabaseContains thenTheDatabaseContains = new ThenTheDatabaseContains(testState, dslContext);
  public final ThenTheCharacterInfoDatabaseContains thenTheCharacterInfoDatabaseContains = new ThenTheCharacterInfoDatabaseContains(testState, testDataProvider);

  @Override
  public Collection<SpecResultListener> getResultListeners() throws Exception {
    return List.of(
        new HtmlResultRenderer()
            .withCustomRenderer(HttpRequest.class, result -> new HttpRequestRenderer())
            .withCustomRenderer(HttpResponse.class, result -> new HttpResponseRenderer())
            .withCustomRenderer(JavaSource.class, result -> new CustomJavaSourceRenderer())
            .withCustomRenderer(SvgWrapper.class, result -> new DontHighlightRenderer<>()),
        new HtmlIndexRenderer()
    );
  }

  // This allows use to start the app once, instead of for each test.
  // Only works if tests are independent
  @BeforeAll
  static void beforeAll() {
    // Instead of starting application each time (per test/test class) during build, can use docker image
    // created before tests are run during build, and the tests are run against this. Will need some logic
    // To check if container is running, then dont start app locally
    application.start();
  }

  @BeforeEach
  void setUp() {
    testDataProvider.deleteAllInfoFromAllTables();
  }

  @AfterAll
  static void afterAll() {
    application.stop();
  }
}
