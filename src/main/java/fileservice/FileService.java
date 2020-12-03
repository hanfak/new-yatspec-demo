package fileservice;

import domain.CharacterDetails;
import domain.Person;
import domain.Species;

import java.io.IOException;

public interface FileService {
  void storeData(String personId, Person characterInfo, Species speciesInfo);

  CharacterDetails readData(String filename) throws IOException;
}
