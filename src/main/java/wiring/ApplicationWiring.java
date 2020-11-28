package wiring;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import databaseservice.CharacterDataProvider;
import fileservice.CounterService;
import fileservice.FileService;
import httpclient.*;
import logging.LoggingCategory;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.Settings;
import thirdparty.AppHttpClient;
import thirdparty.randomjsonservice.RandomXmlService;
import thirdparty.starwarsservice.StarWarsService;
import webserver.JettyWebServer;
import webserver.servlets.*;

import javax.sql.DataSource;

import static databaseservice.DatasourceConfig.createDataSource;
import static org.slf4j.LoggerFactory.getLogger;

// can override methods here in subclass for testing
public class ApplicationWiring {
  final static Logger APPLICATION_LOGGER = getLogger(LoggingCategory.APPLICATION.name());
  private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger(LoggingCategory.AUDIT.name());

  private final Settings settings;
  private final Singletons singletons;
  private final WebserverWiring webserverWiring;

  public static class Singletons {
    final DataSource dataSource;

    public Singletons(DataSource dataSource) {
      this.dataSource = dataSource;
    }
  }

  // Can be used to create object graph for test specific purposes
  ApplicationWiring(Singletons singletons, Settings settings, WebserverWiring webserverWiring) {
    this.singletons = singletons;
    this.settings = settings;
    this.webserverWiring = webserverWiring;
  }

  public static ApplicationWiring wiring(Settings settings) {
    return new ApplicationWiring(new Singletons(createDataSource()), settings, WebserverWiring.webserverWiring());
  }

  public DataSource getDataSource() {
    return singletons.dataSource;
  }

  public DSLContext databaseContextFactory() {
    return DSL.using(getDataSource(), SQLDialect.POSTGRES);
  }

  public JettyWebServer jettyWebServer() {
    return webserverWiring.setupWebServer(this);
  }
  public DataProvider characterDataProvider() {
    return new CharacterDataProvider(databaseContextFactory());
  }

  public AppHttpClient appHttpClient() {
    return new LoggingHttpClient(new DefaultAppHttpClient(), AUDIT_LOGGER, Timer::start, new HttpRequestFormatter(), new HttpResponseFormatter());
  }

  public StarWarsInterfaceService starWarsInterfaceService() {
    return new StarWarsService(appHttpClient(), settings);
  }

  public RandomXmlService randomXmlService() {
    return new RandomXmlService(appHttpClient(), settings);
  }

  public FileService fileService() {
    return new FileService(new CounterService(), new XmlMapper());
  }

  public UseCaseServlet useCaseServlet() {
    return new UseCaseServlet(starWarsInterfaceService(), characterDataProvider(), fileService());
  }

  public UseCaseOneServlet useCaseOneServlet() {
    return new UseCaseOneServlet();
  }

  public UseCaseTwoServlet useCaseTwoServlet() {
    return new UseCaseTwoServlet(characterDataProvider());
  }

  public UseCaseThreeServlet useCaseThreeServlet() {
    return new UseCaseThreeServlet(characterDataProvider());
  }

  public UseCaseFourServlet useCaseFourServlet() {
    return new UseCaseFourServlet(characterDataProvider());
  }

  public UseCaseFiveServlet useCaseFiveServlet() {
    return new UseCaseFiveServlet(characterDataProvider());
  }

  public UseCaseSixServlet useCaseSixServlet() {
    return new UseCaseSixServlet(starWarsInterfaceService(), characterDataProvider());
  }

  public UseCaseSevenServlet useCaseSevenServlet() {
    return new UseCaseSevenServlet(starWarsInterfaceService(), characterDataProvider());
  }

  public UseCaseEightServlet useCaseEightServlet() {
    return new UseCaseEightServlet(starWarsInterfaceService(), randomXmlService(), characterDataProvider());
  }
}
