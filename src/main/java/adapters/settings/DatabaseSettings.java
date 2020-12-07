package adapters.settings;

public interface DatabaseSettings {
  String jdbcUrl();
  String databaeUsername();
  String databasePassword();
  int maxDatabasePoolSize();

}
