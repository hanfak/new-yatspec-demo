package settings;

import jmsservice.QueueName;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.Properties;

import static java.lang.String.format;

// TODO interface for fileservice config etc
// Having multiple settings interface allows users to be specific, plus can create new interfaces which extend
// several interfaces, and users can use this.
public class Settings implements ResponseLetterSettings, StarWarsApiSettings, RandomJsonApiSettings, DatabaseSettings, ActiveMQSettings {

  private final EnhancedProperties properties;

  public Settings(Properties properties, Logger logger) {
    this.properties = new EnhancedProperties(properties, logger);
  }

  @Override
  public String starWarsApiAddress() {
    return properties.getPropertyOrDefaultValue("star.wars.character.info.api", "http://swapi.py4e.com/api/");
  }

  @Override
  public String randomJsonApiAddress() {
    return properties.getPropertyOrDefaultValue("random.xml.books.api", "http://fakerestapi.azurewebsites.net/api/Activities/");
  }

  @Override
  public String responseLetterTemplatePath() {
    return properties.getPropertyOrDefaultValue("response.letter.template.path", "target/classes/files/template.txt");
  }

  @Override
  public String responseLetterFilenameTemplate() {
    return properties.getPropertyOrDefaultValue("response.letter.template.filename", "responseLetter%s.txt");
  }

  @Override
  public String jdbcUrl() {
    return properties.getPropertyOrDefaultValue("jdbc.url", "jdbc:postgresql://127.0.0.1:5432/starwarslocal");
  }

  @Override
  public String databaeUsername() {
    return properties.getPropertyOrDefaultValue("database.username", "postgres");
  }

  @Override
  public String databasePassword() {
    return properties.getPropertyOrDefaultValue("database.password", "docker");
  }

  @Override
  public int maxDatabasePoolSize() {
    return Integer.parseInt(properties.getPropertyOrDefaultValue("database.max.database.pool.size", "1"));
  }

  @Override
  public String brokerUrl() {
    return properties.getPropertyOrThrowRuntimeException("broker.url");
  }

  @Override
  public Optional<String> getActiveSiteForConsumer(QueueName queueName) {
    return properties.getOptionalProperty(String.format("application.queue.%s.active.consumer.site", queueName.getActiveMQDestination().getPhysicalName().toLowerCase()));
  }

  @Override
  public int getMaxConsumersFor(QueueName queueName) {
    String propertyKey = format("application.queue.%s.max.consumers", queueName.getActiveMQDestination().getPhysicalName().toLowerCase());
    return Integer.parseInt(properties.getPropertyOrDefaultValue(propertyKey, "1"));
  }

  public int webserverPort() {
    return Integer.parseInt(properties.getPropertyOrDefaultValue("webserver.port", "2222"));
  }
}
