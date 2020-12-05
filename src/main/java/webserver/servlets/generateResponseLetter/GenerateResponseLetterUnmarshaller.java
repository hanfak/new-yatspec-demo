package webserver.servlets.generateResponseLetter;

import com.fasterxml.jackson.databind.ObjectMapper;
import webserver.servlets.Unmarshaller;

import javax.servlet.ServletInputStream;
import java.io.IOException;

import static fileservice.InputStreamReader.readInputStream;

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
