package adapters.databaseservice;

import adapters.settings.Settings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatasourceConfig {
  private DatasourceConfig() {
  }

  //TODO change type to HikariDataSource
  public static DataSource createDataSource(Settings settings) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(settings.jdbcUrl());
    config.setUsername(settings.databaeUsername());
    config.setPassword(settings.databasePassword());
    config.setAutoCommit(true);
    config.setMaximumPoolSize(settings.maxDatabasePoolSize());
    return new HikariDataSource(config);
  }
}
