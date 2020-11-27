package testinfrastructure;

import acceptancetests._01reqandresponly.thens.ThenTheResponse;
import acceptancetests._01reqandresponly.whens.WhenARequestIsMadeTo;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import testinfrastructure.renderers.CustomJavaSourceRenderer;
import testinfrastructure.renderers.HttpRequestRenderer;
import testinfrastructure.renderers.HttpResponseRenderer;
import wiring.Application;
import wiring.ApplicationWiring;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static settings.PropertyLoader.load;

@ExtendWith(SequenceDiagramExtension.class)
public class AcceptanceTest implements WithCustomResultListeners {

  protected static final String APP = "app";
  protected static final String CLIENT = "client";
  protected static final String STAR_WARS_API = "starWarsApi";
  protected static final String XML_SERVICE = "xmlService";

  protected static final Participant APP_PARTICIPANT = Participants.PARTICIPANT.create(APP);
  protected static final Participant CLIENT_ACTOR = Participants.ACTOR.create(CLIENT);

  protected static final Participant STAR_WARS_API_PARTICIPANT = Participants.PARTICIPANT.create(STAR_WARS_API);
  protected static final Participant XML_SERVICE_PARTICIPANT = Participants.PARTICIPANT.create(XML_SERVICE);

  protected static final String RESPONSE_FORMAT = "Response from %s to %s";
  protected static final String REQUEST_FORMAT = "Request from %s to %s";

  public static final String REQUEST_FROM_CLIENT_TO_APP = format(REQUEST_FORMAT, CLIENT, APP);
  public static final String RESPONSE_FROM_APP_TO_CLIENT = format(RESPONSE_FORMAT, APP, CLIENT);

  public static final String REQUEST_FROM_APP_TO_STAR_WARS_API = format(REQUEST_FORMAT, APP, STAR_WARS_API);
  public static final String RESPONSE_FROM_STAR_WARS_API_TO_APP = format(RESPONSE_FORMAT, STAR_WARS_API, APP);

  public static final String REQUEST_FROM_APP_TO_XML_SERVICE = format(REQUEST_FORMAT, APP, XML_SERVICE);
  public static final String RESPONSE_FROM_XML_SERVICE_TO_APP = format(RESPONSE_FORMAT, XML_SERVICE, APP);

  public final TestState testState = new TestState();

  private final TestDataProvider testDataProvider = new TestDataProvider();
  public final WhenARequestIsMadeTo whenARequestIsMadeTo = new WhenARequestIsMadeTo(testState);
  public final ThenTheResponse thenTheResponse = new ThenTheResponse(whenARequestIsMadeTo::getHttpResponse);
  private final Application application = new Application(ApplicationWiring.wiring(load("target/test-classes/application.prod.properties")));

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

  // Instead of starting application each time during build, can use docker image
  // created before tests are run during build, and the tests are run against this. Will need some logic
  // To check if container is running, then dont start app locally
  @BeforeEach
  void setUp() {
    // start wiremock
    application.start();
    testDataProvider.deleteAllInfoFromAllTables();
  }

  @AfterEach
  void tearDown() {
    application.stop();

    // stop wiremock
  }
}
