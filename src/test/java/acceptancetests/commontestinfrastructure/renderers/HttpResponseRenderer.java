package acceptancetests.commontestinfrastructure.renderers;

import adapters.outgoing.httpclient.HttpResponseFormatter;
import com.googlecode.yatspec.rendering.Renderer;

import java.net.http.HttpResponse;


@SuppressWarnings("rawtypes")
public class HttpResponseRenderer implements Renderer<HttpResponse> {

  @SuppressWarnings("unchecked")
  @Override
  public String render(HttpResponse httpResponse) {
    return new HttpResponseFormatter().formatResponse((HttpResponse<String>) httpResponse);
  }
}
