package webserver.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletInputStream;
import java.io.IOException;

import static fileservice.InputStreamReader.readInputStream;

public class GenerateResponseLetterUnmarshaller {
  ReadFileUpdaterDTO unmarshall(ServletInputStream inputStream) {
    String body = readInputStream(inputStream);
    try {
      return new ObjectMapper().readValue(body, ReadFileUpdaterDTO.class);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }


}
