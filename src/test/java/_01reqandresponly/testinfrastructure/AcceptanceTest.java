package _01reqandresponly.testinfrastructure;

import _01reqandresponly.whens.WhenARequestIsMadeToBuilder;
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
import org.junit.jupiter.api.AfterEach;
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

  public final TestState testState = new TestState();

  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  private final Application application = new Application(ApplicationWiring.wiring(load("target/test-classes/application.test.properties"), APPLICATION_LOGGER));

  public final WhenARequestIsMadeToBuilder whenARequest = new WhenARequestIsMadeToBuilder(testState);


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

  @BeforeEach
  void setUp() {
    application.start();
  }

  @AfterEach
  void tearDown() {
    application.stop();
  }
}
