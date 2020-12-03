package fileservice;

import java.nio.file.Path;

public interface FilenameService {
  Path createNameSpecificFilename(String path, String name);
}
