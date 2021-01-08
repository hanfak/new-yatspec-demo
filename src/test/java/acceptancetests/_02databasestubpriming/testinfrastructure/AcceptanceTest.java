package acceptancetests._02databasestubpriming.testinfrastructure;

import acceptancetests._02databasestubpriming.givens.GivenTheDatabaseContains;
import acceptancetests._02databasestubpriming.testinfrastructure.renderers.CharacterInfoInDatabaseRenderer;
import acceptancetests._02databasestubpriming.testinfrastructure.renderers.SpeciesInfoInDatabaseRendererVersion2;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.CharacterInfoRecord;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.CharacterRecord;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.DatabaseStubFactory;
import acceptancetests._02databasestubpriming.testinfrastructure.stub.SpeciesInfoRecord;
import acceptancetests._02databasestubpriming.thens.ThenTheCharacterInfoDatabaseContains;
import acceptancetests._02databasestubpriming.thens.ThenTheDatabaseContains;
import acceptancetests._02databasestubpriming.thens.ThenTheResponse;
import acceptancetests._02databasestubpriming.whens.WhenARequestIsMadeToBuilder;
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
import wiring.DataRespositoryFactoryInterface;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static adapters.settings.PropertyLoader.load;
import static java.util.Optional.empty;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(SequenceDiagramExtension.class)
public class AcceptanceTest implements WithCustomResultListeners {

  private static final String TEST_PROPERTIES_PATH = "target/test-classes/application.test.properties";

  public final TestState testState = new TestState();

  private static final Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  private static final Map<Integer, CharacterRecord> characterDatabase = new HashMap<>();
  private static final Map<Integer, CharacterInfoRecord> characterInfoDatabase = new HashMap<>();
  private static final Map<Integer, SpeciesInfoRecord> speciesInfoDatabase = new HashMap<>();
  private static final AtomicInteger characterInfoDatabaseId = new AtomicInteger(1);

  private static final DataRespositoryFactoryInterface databaseStub = new DatabaseStubFactory(characterDatabase, characterInfoDatabase, speciesInfoDatabase, characterInfoDatabaseId);
  private static final Application application = new Application(ApplicationWiring.wiringWithCustomAdapters(load(TEST_PROPERTIES_PATH), APPLICATION_LOGGER, databaseStub, empty(), empty(), empty()));
  private final TestDataProvider testDataProvider = new TestDataProvider(characterDatabase, characterInfoDatabase, speciesInfoDatabase, characterInfoDatabaseId);

  public final GivenTheDatabaseContains givenTheDatabaseContains = new GivenTheDatabaseContains(testState, testDataProvider);

  public final WhenARequestIsMadeToBuilder whenARequest = new WhenARequestIsMadeToBuilder(testState);

  public final ThenTheResponse thenReturnedResponse = new ThenTheResponse(whenARequest::getHttpResponse);
  public final ThenTheDatabaseContains thenTheDatabaseContains = new ThenTheDatabaseContains(testState, speciesInfoDatabase);
  public final ThenTheCharacterInfoDatabaseContains thenTheCharacterInfoDatabaseContains = new ThenTheCharacterInfoDatabaseContains(testState, testDataProvider);

  @Override
  public Collection<SpecResultListener> getResultListeners() throws Exception {
    return List.of(
        new HtmlResultRenderer()
            .withCustomRenderer(HttpRequest.class, result -> new HttpRequestRenderer())
            .withCustomRenderer(HttpResponse.class, result -> new HttpResponseRenderer())
            .withCustomRenderer(JavaSource.class, result -> new CustomJavaSourceRenderer())
            .withCustomRenderer(SpeciesInfoRecord.class, result -> new SpeciesInfoInDatabaseRendererVersion2())
            .withCustomRenderer(CharacterInfoRecord.class, result -> new CharacterInfoInDatabaseRenderer())
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
    testDataProvider.deleteAllInfoFromAllTables(); // now clearing data from stub
  }

  @AfterAll
  static void afterAll() {
    application.stop();
  }
}
