package settings;

import org.slf4j.Logger;

import java.util.Properties;

import static java.lang.String.format;

public class EnhancedProperties {

  private final Properties properties;
  private final Logger logger;

  public EnhancedProperties(Properties properties, Logger logger) {
    this.properties = properties;
    this.logger = logger;
  }

  public String getPropertyOrDefaultValue(String propertyName, String defaultValue) {
    String property = properties.getProperty(propertyName);
    if (property != null) {
      return property;
    }
    logger.warn(format("The property %s was not set, defaulting to %s", propertyName, defaultValue));
    return defaultValue;
  }
}
