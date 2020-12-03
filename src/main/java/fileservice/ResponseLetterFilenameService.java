package fileservice;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class ResponseLetterFilenameService implements FilenameService {

  @Override
  public Path createNameSpecificFilename(String path, String name) {
    return Paths.get(
        stream(path.split("\\.")).collect(joining(name + ".")));
  }
}
