package adapters.incoming.webserver.servlets.exteranlcalls;

import adapters.incoming.webserver.servlets.Marshaller;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ExternalCallExampleOneMarshaller implements Marshaller<ExternalCallExampleOutgoingDTO, String> {

  @Override
  public String marshall(ExternalCallExampleOutgoingDTO dataToMarshall) {
    try {
      return new ObjectMapper().writeValueAsString(dataToMarshall);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
