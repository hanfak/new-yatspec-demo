package adapters.outgoing.fileservice;

import core.domain.Person;
import core.domain.Species;

import java.io.IOException;

public interface FileService {
  void storeData(String personId, Person characterInfo, Species speciesInfo);

  CharacterDetails readData(String filename) throws IOException;
}
