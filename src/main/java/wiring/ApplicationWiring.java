package wiring;

import async.ExecutorServiceAsyncProcessor;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import databaseservice.CharacterDataProvider;
import fileservice.*;
import httpclient.*;
import logging.LoggingCategory;
import org.eclipse.jetty.server.Server;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.Settings;
import thirdparty.AppHttpClient;
import thirdparty.randomjsonservice.RandomXmlService;
import thirdparty.starwarsservice.StarWarsService;
import usecases.generateresponseletter.GenerateResponseLetterUseCase;
import usecases.generateresponseletter.GenerateResponseLetterUseCasePort;
import usecases.generateresponseletter.ResponseLetterReplacer;
import webserver.JettyWebServer;
import webserver.servlets.*;
import webserver.servlets.generateResponseLetter.GenerateResponseLetterUnmarshaller;
import webserver.servlets.generateResponseLetter.GenerateResponseLetterUseCaseServlet;

import javax.sql.DataSource;

import static databaseservice.DatasourceConfig.createDataSource;

// can override methods here in subclass for testing
public class ApplicationWiring {

  private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger(LoggingCategory.AUDIT.name());

  private final Settings settings;
  private final Singletons singletons;
  private final Logger applicationLogger;

  private static class Singletons {
    final DataSource dataSource;
    final WebserverWiring webserverWiring;

    public Singletons(DataSource dataSource, WebserverWiring webserverWiring) {
      this.dataSource = dataSource;
      this.webserverWiring = webserverWiring;
    }
  }

  // Can be used to create object graph for test specific purposes
  private ApplicationWiring(Singletons singletons, Settings settings, Logger applicationLogger) {
    this.singletons = singletons;
    this.settings = settings;
    this.applicationLogger = applicationLogger;
  }

  public static ApplicationWiring wiring(Settings settings, Logger applicationLogger) {
    JettyWebServer jettyWebServer = new JettyWebServer(applicationLogger, new Server(settings.webserverPort()));
    WebserverWiring webserverWiring = WebserverWiring.webserverWiring(jettyWebServer);
    Singletons singletons = new Singletons(createDataSource(settings), webserverWiring);
    return new ApplicationWiring(singletons, settings, applicationLogger);
  }

  public DataSource getDataSource() {
    return singletons.dataSource;
  }

  public DSLContext databaseContextFactory() {
    return DSL.using(getDataSource(), SQLDialect.POSTGRES);
  }

  JettyWebServer jettyWebServer() {
    return singletons.webserverWiring.setupWebServer(this); // TODO: Fix passing this object
  }

  // Can be overridden for tests
  public DataProvider characterDataProvider() {
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

  // Can be overridden for tests
  public FileService fileService() {
    return new MyFileService(new InMemoryIdService(), new XmlMapper(), applicationLogger);
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

  // Can be overridden for tests
  public FileSystemWriter fileWriter() {
    return new FileSystemWriter(applicationLogger);
  }

  private GenerateResponseLetterUseCasePort generateResponseLetterUseCase() {
    return new GenerateResponseLetterUseCase(new ResponseLetterReplacer(), new FileSystemFileReader(),
        fileWriter(), new InMemoryIdService(), settings, applicationLogger);
  }

  GenerateResponseLetterUseCaseServlet generateResponseLetterUseCaseServlet() {
    return new GenerateResponseLetterUseCaseServlet(new GenerateResponseLetterUnmarshaller(), generateResponseLetterUseCase(), new ExecutorServiceAsyncProcessor());
  }
}
