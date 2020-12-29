package _01reqandresponly.testinfrastructure;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;

public class TestDataProvider {
  private final DataSource dataSource;

  public TestDataProvider(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void deleteAllInfoFromAllTables() {
    DSLContext dslContext = DSL.using(dataSource, SQLDialect.POSTGRES);
    dslContext.deleteFrom(CHARACTERINFO).execute();
    dslContext.deleteFrom(SPECIFIESINFO).execute();
    dslContext.deleteFrom(CHARACTERS).execute();
  }
}
