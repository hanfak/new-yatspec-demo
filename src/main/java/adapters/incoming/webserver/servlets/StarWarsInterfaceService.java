package adapters.incoming.webserver.servlets;

import core.domain.Person;
import core.domain.Species;

public interface StarWarsInterfaceService {
  Person getCharacterInfo(String id);

  Species getSpeciesInfo(String url);
}
