package adapters.webserver.servlets;

import core.domain.Person;
import core.domain.Species;

import java.io.IOException;

public interface StarWarsInterfaceService {
  Person getCharacterInfo(String id) throws IOException;

  Species getSpeciesInfo(String url) throws IOException;
}
