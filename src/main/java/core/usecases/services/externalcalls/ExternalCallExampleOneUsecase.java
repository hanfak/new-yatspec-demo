package core.usecases.services.externalcalls;

import adapters.incoming.webserver.servlets.StarWarsInterfaceService;
import core.domain.Person;
import core.usecases.ports.incoming.ExternalCallExampleOneService;

public class ExternalCallExampleOneUsecase implements ExternalCallExampleOneService {

  private final StarWarsInterfaceService starWarsInterfaceService;

  public ExternalCallExampleOneUsecase(StarWarsInterfaceService starWarsInterfaceService) {
    this.starWarsInterfaceService = starWarsInterfaceService;
  }

  @Override
  public Output execute(String personId) {
    Person person = starWarsInterfaceService.getCharacterInfo(personId);
    return new Output(person.getName().toUpperCase(), person.getBirthYear());
  }
}
