package acceptancetests.databasepriming.testinfrastructure;

import acceptancetests.databasepriming.givens.GivenTheDatabaseContains;
import acceptancetests.databasepriming.testinfrastructure.renderers.CustomJavaSourceRenderer;
import acceptancetests.databasepriming.testinfrastructure.renderers.HttpRequestRenderer;
import acceptancetests.databasepriming.testinfrastructure.renderers.HttpResponseRenderer;
import acceptancetests.reqandresponly.thens.ThenTheResponse;
import acceptancetests.reqandresponly.thens.ThenTheResponseVersion2;
import acceptancetests.reqandresponly.whens.WhenARequestIsMadeTo;
import acceptancetests.reqandresponly.whens.WhenARequestIsMadeToBuilder;
import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.sequence.Participant;
import com.googlecode.yatspec.sequence.Participants;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import databaseservice.DatasourceConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import wiring.Application;

import javax.sql.DataSource;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;

@ExtendWith(SequenceDiagramExtension.class)
public class AcceptanceTest implements WithCustomResultListeners {

  protected static final String APP = "app";
  protected static final String CLIENT = "client";

  protected static final Participant APP_PARTICIPANT = Participants.PARTICIPANT.create(APP);
  protected static final Participant CLIENT_ACTOR = Participants.ACTOR.create(CLIENT);

  protected static final String RESPONSE_FORMAT = "Response from %s to %s";
  protected static final String REQUEST_FORMAT = "Request from %s to %s";

  public static final String REQUEST_FROM_CLIENT_TO_APP = format(REQUEST_FORMAT, CLIENT, APP);
  public static final String RESPONSE_FROM_APP_TO_CLIENT = format(RESPONSE_FORMAT, APP, CLIENT);

  public final TestState testState = new TestState();

  public final DataSource dataSource = DatasourceConfig.createDataSource();
  private final TestDataProvider testDataProvider = new TestDataProvider(dataSource);
  public final GivenTheDatabaseContains givenTheDatabaseContains = new GivenTheDatabaseContains(dataSource);
  public final WhenARequestIsMadeTo whenARequestIsMadeTo = new WhenARequestIsMadeTo(testState);
  public final ThenTheResponse thenTheResponse = new ThenTheResponse(whenARequestIsMadeTo::getHttpResponse);

  public final WhenARequestIsMadeToBuilder whenARequest = new WhenARequestIsMadeToBuilder(testState);
  public final ThenTheResponse thenResponse = new ThenTheResponse(whenARequest::getHttpResponse);
  public final ThenTheResponseVersion2 thenReturnedResponse = new ThenTheResponseVersion2(whenARequest::getHttpResponse);

  private final Application application = new Application();

  @Override
  public Collection<SpecResultListener> getResultListeners() throws Exception {
    return List.of(
            new HtmlResultRenderer()
                    .withCustomRenderer(HttpRequest.class, new HttpRequestRenderer())
                    .withCustomRenderer(HttpResponse.class, new HttpResponseRenderer())
                    .withCustomRenderer(JavaSource.class, new CustomJavaSourceRenderer())
                    .withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer<>()),
            new HtmlIndexRenderer()
    );  }

  @BeforeEach
  void setUp() {
    // Instead of starting application each time during build, can use docker image
    // created before tests are run during build, and the tests are run against this. Will need some logic
    // To check if container is running, then dont start app locally
    application.start("target/test-classes/application.test.properties");
    testDataProvider.deleteAllInfoFromAllTables();
  }

  @AfterEach
  void tearDown() {
    application.stop();
  }
}
