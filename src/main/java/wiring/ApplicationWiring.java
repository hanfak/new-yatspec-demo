package wiring;

import adapters.async.ExecutorServiceAsyncProcessor;
import adapters.incoming.jmslistener.instructions.JsonInstructionsFactory;
import adapters.incoming.webserver.JettyWebServer;
import adapters.incoming.webserver.servlets.*;
import adapters.incoming.webserver.servlets.generateResponseLetter.GenerateResponseLetterUnmarshaller;
import adapters.incoming.webserver.servlets.generateResponseLetter.GenerateResponseLetterUseCaseServlet;
import adapters.incoming.webserver.servlets.jmsexample.JmsExampleOneServlet;
import adapters.logging.LoggingCategory;
import adapters.outgoing.databaseservice.CharacterDataProvider;
import adapters.outgoing.fileservice.FileService;
import adapters.outgoing.fileservice.FileSystemWriter;
import adapters.outgoing.fileservice.InMemoryIdService;
import adapters.outgoing.fileservice.MyFileService;
import adapters.outgoing.httpclient.*;
import adapters.outgoing.jmssender.ActiveMqMessageSender;
import adapters.outgoing.jmssender.AuditMessageSender;
import adapters.outgoing.thirdparty.AppHttpClient;
import adapters.outgoing.thirdparty.randomjsonservice.RandomXmlService;
import adapters.outgoing.thirdparty.starwarsservice.StarWarsService;
import adapters.settings.internal.Settings;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import core.usecases.ports.incoming.GenerateResponseLetterUseCasePort;
import core.usecases.ports.outgoing.MessageSender;
import core.usecases.services.jmsexample.UseCaseExampleOneStepOne;
import org.eclipse.jetty.server.Server;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.sql.DataSource;

import static adapters.outgoing.databaseservice.DatasourceConfig.createDataSource;
import static wiring.JmsWiring.jmsWiring;
import static wiring.WebserverWiring.webserverWiring;

// can override methods here in subclass for testing
public class ApplicationWiring {

  private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger(LoggingCategory.AUDIT.name());

  private final Settings settings;
  private final UseCaseFactory useCaseFactory;
  private final Singletons singletons;
  private final Logger applicationLogger;

  private static class Singletons {
    final DataSource dataSource;
    final WebserverWiring webserverWiring;
    final JmsWiring jmsWiring;

    public Singletons(DataSource dataSource, WebserverWiring webserverWiring, JmsWiring jmsWiring) {
      this.dataSource = dataSource;
      this.webserverWiring = webserverWiring;
      this.jmsWiring = jmsWiring;
    }
  }

  public ApplicationWiring() {
    throw new AssertionError("Should not be instantiated outside of static factory method");
  }

  private ApplicationWiring(UseCaseFactory useCaseFactory, Singletons singletons, Settings settings, Logger applicationLogger) {
    this.useCaseFactory = useCaseFactory;
    this.singletons = singletons;
    this.settings = settings;
    this.applicationLogger = applicationLogger;
  }

  public static ApplicationWiring wiring(Settings settings, Logger applicationLogger) {
    JettyWebServer jettyWebServer = new JettyWebServer(applicationLogger, new Server(settings.webserverPort()));
    WebserverWiring webserverWiring = webserverWiring(jettyWebServer);
    UseCaseFactory useCaseFactory = new UseCaseFactory(applicationLogger, settings);
    JmsWiring jmsWiring = jmsWiring(useCaseFactory, settings, applicationLogger, AUDIT_LOGGER);
    Singletons singletons = new Singletons(createDataSource(settings), webserverWiring, jmsWiring);
    return new ApplicationWiring(useCaseFactory, singletons, settings, applicationLogger);
  }

  public void setupJmsListeners() {
    singletons.jmsWiring.addConsumerConfiguration();
  }

  public void stopJmsListeners() {
    singletons.jmsWiring.stopListeners();
  }

  public void startJmsListeners() {
    singletons.jmsWiring.startListeners();
  }

  public DataSource getDataSource() {
    return singletons.dataSource;
  }

  JettyWebServer jettyWebServer() {
    return singletons.webserverWiring.setupWebServer(this); // TODO: Fix passing this object
  }

  // TODO extract to database wiring
  public DSLContext databaseContextFactory() {
    return DSL.using(getDataSource(), SQLDialect.POSTGRES);
  }

  // Can be overridden for tests
  public DataProvider characterDataProvider() {
    return new CharacterDataProvider(databaseContextFactory());
  }
  // TODO extract to database wiring

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

  // Can be overridden for tests
  public FileSystemWriter fileWriter() {
    return new FileSystemWriter(applicationLogger);
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

  GenerateResponseLetterUseCaseServlet generateResponseLetterUseCaseServlet() {
    GenerateResponseLetterUseCasePort generateResponseLetterUseCasePort = useCaseFactory.generateResponseLetterUseCase(fileWriter());
    return new GenerateResponseLetterUseCaseServlet(new GenerateResponseLetterUnmarshaller(), generateResponseLetterUseCasePort, new ExecutorServiceAsyncProcessor());
  }

  JmsExampleOneServlet jmsExampleOneServlet() {
    JmsTemplate jmsTemplate = new JmsTemplate(singletons.jmsWiring.activeMQConnectionFactory());
    MessageSender messageSender = new AuditMessageSender(new ActiveMqMessageSender(jmsTemplate), AUDIT_LOGGER);

    return new JmsExampleOneServlet(new UseCaseExampleOneStepOne(messageSender, jsonInstructionFactory(), applicationLogger));
  }

  private JsonInstructionsFactory jsonInstructionFactory() {
    return new JsonInstructionsFactory();
  }
}
