package acceptancetests._01reqandresponly.testinfrastructure;

import acceptancetests._01reqandresponly.testinfrastructure.renderers.CustomJavaSourceRenderer;
import acceptancetests._01reqandresponly.testinfrastructure.renderers.HttpRequestRenderer;
import acceptancetests._01reqandresponly.testinfrastructure.renderers.HttpResponseRenderer;
import acceptancetests._01reqandresponly.thens.ThenTheResponse;
import acceptancetests._01reqandresponly.thens.ThenTheResponseVersion2;
import acceptancetests._01reqandresponly.whens.WhenARequestIsMadeTo;
import acceptancetests._01reqandresponly.whens.WhenARequestIsMadeToBuilder;
import com.googlecode.yatspec.junit.SequenceDiagramExtension;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import logging.LoggingCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import wiring.Application;
import wiring.ApplicationWiring;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static settings.PropertyLoader.load;

@ExtendWith(SequenceDiagramExtension.class)
public class AcceptanceTest implements WithCustomResultListeners {

  public final TestState testState = new TestState();

  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  private final Application application = new Application(ApplicationWiring.wiring(load("target/test-classes/application.test.properties"), APPLICATION_LOGGER));
  private final TestDataProvider testDataProvider = new TestDataProvider(application.getDataSource());
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
    // Instead of starting application each time during build, can use docker image
    // created before tests are run during build, and the tests are run against this. Will need some logic
    // To check if container is running, then dont start app locally
    application.start();
    testDataProvider.deleteAllInfoFromAllTables();
  }

  @AfterEach
  void tearDown() {
    application.stop();
  }
}
