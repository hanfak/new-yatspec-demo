package adapters.incoming.webserver.servlets.aggregate;

import adapters.incoming.webserver.servlets.Unmarshaller;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletInputStream;
import java.io.IOException;

import static adapters.outgoing.fileservice.InputStreamReader.readInputStream;

public class AggregateExampleOneUnmarshaller implements Unmarshaller<ServletInputStream, IncomingAggregateDTO> {

  @Override
  public IncomingAggregateDTO unmarshall(ServletInputStream inputStream) {
    String body = readInputStream(inputStream);
    try {
      return new ObjectMapper().readValue(body, IncomingAggregateDTO.class);
    } catch (IOException e) {
      // Form of validation for web layer/adapter
      throw new IllegalArgumentException(e);
    }
  }
}
