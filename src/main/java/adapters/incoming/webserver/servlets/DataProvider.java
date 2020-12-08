package adapters.incoming.webserver.servlets;

import core.domain.Person;
import core.domain.SpeciesInfo;

public interface DataProvider {
  Integer getPersonId(String personName);

  void storeCharacterInfo(String personId, Person characterInfo);

  SpeciesInfo getSpeciesInfo(Integer personId);

  String getBirthYear(int personId);
}
