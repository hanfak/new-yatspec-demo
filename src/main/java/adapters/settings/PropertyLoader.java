package adapters.settings;

import adapters.logging.LoggingCategory;
import adapters.settings.internal.Settings;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public class PropertyLoader {
  private final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());

  public static Settings load(String propertyFile) {
    Path appProperties = Paths.get(propertyFile);
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(appProperties.toFile()));
      APPLICATION_LOGGER.info("loaded application from: " + appProperties);
    } catch (IOException e) {
      String message = format("Could not find application properties at '%s'", propertyFile);
      APPLICATION_LOGGER.error(message, e);
      throw new IllegalStateException(message, e);
    }
    return new Settings(properties, APPLICATION_LOGGER);
  }
}
