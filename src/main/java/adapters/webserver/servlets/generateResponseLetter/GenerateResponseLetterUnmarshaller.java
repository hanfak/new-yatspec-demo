package adapters.webserver.servlets.generateResponseLetter;

import adapters.webserver.servlets.Unmarshaller;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletInputStream;
import java.io.IOException;

import static adapters.fileservice.InputStreamReader.readInputStream;

public class GenerateResponseLetterUnmarshaller implements Unmarshaller<ServletInputStream, ReadFileUpdaterDTO> {

  @Override
  public ReadFileUpdaterDTO unmarshall(ServletInputStream inputStream) {
    String body = readInputStream(inputStream);
    try {
      return new ObjectMapper().readValue(body, ReadFileUpdaterDTO.class);
    } catch (IOException e) {
      // Form of validation for web layer/adapter
      throw new IllegalArgumentException(e);
    }
  }
}
