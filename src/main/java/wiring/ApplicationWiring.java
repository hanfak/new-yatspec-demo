package wiring;

import adapters.async.ExecutorServiceAsyncProcessor;
import adapters.incoming.webserver.JettyWebServer;
import adapters.incoming.webserver.servlets.*;
import adapters.incoming.webserver.servlets.aggregate.AggregateExampleOneServlet;
import adapters.incoming.webserver.servlets.aggregate.AggregateExampleOneUnmarshaller;
import adapters.incoming.webserver.servlets.exteranlcalls.ExternalCallExampleOneMarshaller;
import adapters.incoming.webserver.servlets.exteranlcalls.ExternalCallExampleOneServlet;
import adapters.incoming.webserver.servlets.generateResponseLetter.GenerateResponseLetterUnmarshaller;
import adapters.incoming.webserver.servlets.generateResponseLetter.GenerateResponseLetterUseCaseServlet;
import adapters.incoming.webserver.servlets.jmsexample.JmsExampleOneServlet;
import adapters.incoming.webserver.servlets.jmsexample.JmsExampleTwoServlet;
import adapters.logging.LoggingCategory;
import adapters.outgoing.fileservice.FileService;
import adapters.outgoing.fileservice.InMemoryIdService;
import adapters.outgoing.fileservice.MyFileService;
import adapters.outgoing.httpclient.*;
import adapters.outgoing.thirdparty.AppHttpClient;
import adapters.outgoing.thirdparty.randomjsonservice.RandomXmlService;
import adapters.outgoing.thirdparty.starwarsservice.StarWarsService;
import adapters.settings.internal.Settings;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import core.usecases.ports.incoming.GenerateResponseLetterUseCasePort;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import static adapters.outgoing.databaseservice.DatasourceConfig.createDataSource;
import static wiring.JmsWiring.jmsWiring;
import static wiring.UseCaseFactory.useCaseFactory;
import static wiring.WebserverWiring.webserverWiring;

// TODO Fix wiring, use singleton correctly
// can override methods here in subclass for testing
public class ApplicationWiring {

  private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger(LoggingCategory.AUDIT.name());

  private final Settings settings;
  private final UseCaseFactory useCaseFactory;
  private final JmsWiring jmsWiring;
  private final Singletons singletons;
  private final Logger applicationLogger;

  private static class Singletons {

    final DataSource dataSource;
    final WebserverWiring webserverWiring;
    final DataRespositoryFactoryInterface dataRepositoryFactory;

    public Singletons(DataSource dataSource, WebserverWiring webserverWiring, DataRespositoryFactoryInterface dataRepositoryFactory) {
      this.dataSource = dataSource;
      this.webserverWiring = webserverWiring;
      this.dataRepositoryFactory = dataRepositoryFactory;
    }
  }

  public ApplicationWiring() {
    throw new AssertionError("Should not be instantiated outside of static factory method");
  }

  private ApplicationWiring(UseCaseFactory useCaseFactory, JmsWiring jmsWiring, Singletons singletons, Settings settings, Logger applicationLogger) {
    this.useCaseFactory = useCaseFactory;
    this.jmsWiring = jmsWiring;
    this.singletons = singletons;
    this.settings = settings;
    this.applicationLogger = applicationLogger;
  }

  // For running application
  public static ApplicationWiring wiring(Settings settings, Logger applicationLogger) {
    DataSource dataSource = createDataSource(settings);
    return wiringWithCustomAdapters(settings, applicationLogger, new DataRepositoryFactory(dataSource, applicationLogger), new FileIoFactory(applicationLogger), true, dataSource);
  }

  // For Testing, can pass in own databaseFactory (can inherit from prod and add extra database methods for testing)
  public static ApplicationWiring wiringWithCustomAdapters(Settings settings, Logger applicationLogger, DataRespositoryFactoryInterface dataRepositoryFactory, FileIoFactory fileIoFactory, Boolean dataSourceUsed, DataSource dataSource) {
    JettyWebServer jettyWebServer = new JettyWebServer(applicationLogger, new Server(settings.webserverPort()));
    WebserverWiring webserverWiring = webserverWiring(jettyWebServer);
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(settings.brokerUrl());
    UseCaseFactory useCaseFactory = useCaseFactory(fileIoFactory, dataRepositoryFactory, applicationLogger, settings, activeMQConnectionFactory, AUDIT_LOGGER);
    JmsWiring jmsWiring = jmsWiring(useCaseFactory, activeMQConnectionFactory, settings, applicationLogger, AUDIT_LOGGER);
    Singletons singletons = new Singletons(dataSourceUsed ? createDataSource(settings) : null, webserverWiring, dataRepositoryFactory);
    return new ApplicationWiring(useCaseFactory, jmsWiring, singletons, settings, applicationLogger);
  }

  public void setupJmsListeners() {
    jmsWiring.addConsumerConfiguration();
  }

  public void stopJmsListeners() {
    jmsWiring.stopListeners();
  }

  public void startJmsListeners() {
    jmsWiring.startListeners();
  }

  public DataSource getDataSource() {
    return singletons.dataSource;
  }

  JettyWebServer jettyWebServer() {
    return singletons.webserverWiring.setupWebServer(this); // TODO: Fix passing this object
  }

  // TODO extract to client wiring
  private AppHttpClient appHttpClient() {
    return new LoggingHttpClient(new DefaultAppHttpClient(), AUDIT_LOGGER, Timer::start, new HttpRequestFormatter(), new HttpResponseFormatter());
  }

  private StarWarsInterfaceService starWarsInterfaceService() {
    return new StarWarsService(appHttpClient(), settings);
  }

  private ActivityService randomXmlService() {
    return new RandomXmlService(appHttpClient(), settings);
  }
  // TODO extract to client wiring

  // Can be overridden for tests
  public FileService fileService() {
    return new MyFileService(new InMemoryIdService(), new XmlMapper(), applicationLogger);
  }

  private DataProvider characterDataProvider() {
    return singletons.dataRepositoryFactory.characterDataProvider();
  }

  // TODO extract to servlet factory??
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

  GenerateResponseLetterUseCaseServlet generateResponseLetterUseCaseServlet() {
    GenerateResponseLetterUseCasePort generateResponseLetterUseCasePort = useCaseFactory.generateResponseLetterUseCase();
    return new GenerateResponseLetterUseCaseServlet(new GenerateResponseLetterUnmarshaller(), generateResponseLetterUseCasePort, new ExecutorServiceAsyncProcessor());
  }

  JmsExampleOneServlet jmsExampleOneServlet() {
    return new JmsExampleOneServlet(useCaseFactory.useCaseExampleOneStepOne());
  }

  JmsExampleTwoServlet jmsExampleTwoServlet() {
    return new JmsExampleTwoServlet(useCaseFactory.useCaseExampleTwoStepOne());
  }

  AggregateExampleOneServlet aggregateExampleOneServlet() {
    return new AggregateExampleOneServlet(useCaseFactory.aggregateExample1Step1Service(), new AggregateExampleOneUnmarshaller());
  }

  ExternalCallExampleOneServlet externalCallExampleOneServlet() {
    return new ExternalCallExampleOneServlet(useCaseFactory.externalCallExampleOneService(starWarsInterfaceService()), new ExternalCallExampleOneMarshaller());
  }
}
