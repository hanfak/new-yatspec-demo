package wiring;

import adapters.incoming.webserver.servlets.ActivityService;
import adapters.incoming.webserver.servlets.StarWarsInterfaceService;

public interface ExternalCallWiringInterface {
  StarWarsInterfaceService starWarsInterfaceService();

  ActivityService randomXmlService();
}
