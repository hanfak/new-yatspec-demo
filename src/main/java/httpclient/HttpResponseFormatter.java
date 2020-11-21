package httpclient;

import org.eclipse.jetty.http.HttpStatus;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;


public class HttpResponseFormatter  {

    public String formatResponse(HttpResponse<String> httpResponse) {
        int statusCode = httpResponse.statusCode();
        String responseBody = Optional.ofNullable(httpResponse.body()).orElse("");
        String headers = convertHeadersToDisplayFormat(httpResponse.headers().map().entrySet());

        return format("%d %s\n%s\n\n%s", statusCode, HttpStatus.getMessage(statusCode), headers, responseBody);
    }

    private String convertHeadersToDisplayFormat(Set<Map.Entry<String, List<String>>> entries) {
        return entries.stream()
                .flatMap(this::toHeaderAndValuePairStream)
                .collect(joining("\n"));
    }

    private Stream<String> toHeaderAndValuePairStream(Map.Entry<String, List<String>> headerEntry) {
        return headerEntry.getValue()
                .stream()
                .map(headerValue -> format("%s: %s", headerEntry.getKey(), headerValue));
    }
}
