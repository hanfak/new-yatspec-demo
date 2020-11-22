package acceptancetests.databasepriming.testinfrastructure.renderers;

import com.googlecode.yatspec.rendering.Renderer;
import httpclient.HttpRequestFormatter;

import java.net.http.HttpRequest;


public class HttpRequestRenderer implements Renderer<HttpRequest> {

    @Override
    public String render(HttpRequest httpRequest) {
        return new HttpRequestFormatter().formatRequest(httpRequest);
    }
}

