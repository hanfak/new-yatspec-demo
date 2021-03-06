package acceptancetests._01reqandresponly.testinfrastructure;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

import static org.jooq.sources.Tables.CHARACTERINFO;
import static org.jooq.sources.Tables.CHARACTERS;
import static org.jooq.sources.Tables.SPECIFIESINFO;

// TODO extend CharacterDataProvider, which is newed when app is started in test context
// TODO need to seperate factories into new class, n pass into app.start
//TODO settings for different db name, so prod is not used
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
