package wiring;

import async.ExecutorServiceAsyncProcessor;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import databaseservice.CharacterDataProvider;
import fileservice.*;
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
import usecases.LetterCreator;
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

  private static class Singletons {
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

  protected JettyWebServer jettyWebServer() {
    return webserverWiring.setupWebServer(this);
  }

  private DataProvider characterDataProvider() {
    return new CharacterDataProvider(databaseContextFactory());
  }

  private AppHttpClient appHttpClient() {
    return new LoggingHttpClient(new DefaultAppHttpClient(), AUDIT_LOGGER, Timer::start, new HttpRequestFormatter(), new HttpResponseFormatter());
  }

  private StarWarsInterfaceService starWarsInterfaceService() {
    return new StarWarsService(appHttpClient(), settings);
  }

  private RandomXmlService randomXmlService() {
    return new RandomXmlService(appHttpClient(), settings);
  }

  private FileService fileService() {
    return new MyFileService(new CounterService(), new XmlMapper(), APPLICATION_LOGGER);
  }

  protected UseCaseServlet useCaseServlet() {
    return new UseCaseServlet(starWarsInterfaceService(), characterDataProvider(), fileService());
  }

  protected UseCaseOneServlet useCaseOneServlet() {
    return new UseCaseOneServlet();
  }

  protected UseCaseTwoServlet useCaseTwoServlet() {
    return new UseCaseTwoServlet(characterDataProvider());
  }

  protected UseCaseThreeServlet useCaseThreeServlet() {
    return new UseCaseThreeServlet(characterDataProvider());
  }

  protected UseCaseFourServlet useCaseFourServlet() {
    return new UseCaseFourServlet(characterDataProvider());
  }

  protected UseCaseFiveServlet useCaseFiveServlet() {
    return new UseCaseFiveServlet(characterDataProvider());
  }

  protected UseCaseSixServlet useCaseSixServlet() {
    return new UseCaseSixServlet(starWarsInterfaceService(), characterDataProvider());
  }

  protected UseCaseSevenServlet useCaseSevenServlet() {
    return new UseCaseSevenServlet(starWarsInterfaceService(), characterDataProvider());
  }

  protected UseCaseEightServlet useCaseEightServlet() {
    return new UseCaseEightServlet(starWarsInterfaceService(), randomXmlService(), characterDataProvider());
  }

  protected GenerateResponseLetterUseCaseServlet generateResponseLetterUseCase() {
    LetterCreator letterCreator = new LetterCreator(new ResponseLetterReplacer(), new FileSystemFileReader(), new FileSystemWriter(APPLICATION_LOGGER), new ResponseLetterFilenameService(), APPLICATION_LOGGER);

    return new GenerateResponseLetterUseCaseServlet(new GenerateResponseLetterUnmarshaller(), letterCreator, new ExecutorServiceAsyncProcessor());
  }
}
