package wiring;

import adapters.outgoing.fileservice.FileSystemWriter;
import org.slf4j.Logger;

public class FileIoFactory {

  private final Logger logger;

  public FileIoFactory(Logger logger) {
    this.logger = logger;
  }

  public FileSystemWriter fileWriter() {
    return new FileSystemWriter(logger);
  }
}
