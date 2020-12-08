package adapters.incoming.webserver.servlets;

import adapters.outgoing.thirdparty.randomjsonservice.Activity;

import java.io.IOException;

public interface ActivityService {
  Activity getCharacterInfo(Integer id) throws IOException;
}
