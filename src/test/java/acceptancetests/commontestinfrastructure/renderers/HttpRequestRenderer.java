package acceptancetests.commontestinfrastructure.renderers;

import adapters.outgoing.httpclient.HttpRequestFormatter;
import com.googlecode.yatspec.rendering.Renderer;

import java.net.http.HttpRequest;


public class HttpRequestRenderer implements Renderer<HttpRequest> {

  @Override
  public String render(HttpRequest httpRequest) {
    return new HttpRequestFormatter().formatRequest(httpRequest);
  }
}

