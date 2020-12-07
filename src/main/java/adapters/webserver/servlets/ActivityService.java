package adapters.webserver.servlets;

import adapters.thirdparty.randomjsonservice.Activity;

import java.io.IOException;

public interface ActivityService {
  Activity getCharacterInfo(Integer id) throws IOException;
}
