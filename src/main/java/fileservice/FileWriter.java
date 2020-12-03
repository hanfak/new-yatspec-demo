package fileservice;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

public interface FileWriter {
  Path write(Supplier<Path> path, String fileContents);

  void copy(Supplier<Path> path, InputStream inputStream);

  void copyFileToFileSystem(Supplier<Path> path, Path targetFile);

  Path append(Supplier<Path> path, String fileContents);
}
