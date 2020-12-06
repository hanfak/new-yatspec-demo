package wiring;

import async.ExecutorServiceAsyncProcessor;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import databaseservice.CharacterDataProvider;
import fileservice.*;
import httpclient.*;
import jmsservice.listener.AuditMessageListener;
import jmsservice.listener.configuration.ApplicationQueueConsumerConfiguration;
import jmsservice.listener.configuration.QueueConsumerConfiguration;
import jmsservice.listener.queuelisteners.UseCaseExampleOneStepTwoInstructionListener;
import jmsservice.sender.ActiveMqMessageSender;
import jmsservice.sender.AuditMessageSender;
import jmsservice.sender.MessageSender;
import logging.LoggingCategory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import settings.Settings;
import thirdparty.AppHttpClient;
import thirdparty.randomjsonservice.RandomXmlService;
import thirdparty.starwarsservice.StarWarsService;
import usecases.generateresponseletter.GenerateResponseLetterUseCase;
import usecases.generateresponseletter.GenerateResponseLetterUseCasePort;
import usecases.generateresponseletter.ResponseLetterReplacer;
import usecases.jmsexample.UseCaseExampleOneStepOne;
import usecases.jmsexample.UseCaseExampleOneStepTwo;
import webserver.JettyWebServer;
import webserver.servlets.*;
import webserver.servlets.generateResponseLetter.GenerateResponseLetterUnmarshaller;
import webserver.servlets.generateResponseLetter.GenerateResponseLetterUseCaseServlet;
import webserver.servlets.jmsexample.JmsExampleOneServlet;

import javax.jms.MessageListener;
import javax.sql.DataSource;
import java.util.function.UnaryOperator;

import static databaseservice.DatasourceConfig.createDataSource;
import static jmsservice.QueueName.EXAMPLE_ONE_STEP_ONE_INSTRUCTION;

// can override methods here in subclass for testing
public class ApplicationWiring {

  private final static Logger AUDIT_LOGGER = LoggerFactory.getLogger(LoggingCategory.AUDIT.name());

  private final Settings settings;
  private final Singletons singletons;
  private final Logger applicationLogger;

  private static class Singletons {
    final DataSource dataSource;
    final WebserverWiring webserverWiring;
    final ActiveMQConnectionFactory activeMQConnectionFactory;

    public Singletons(DataSource dataSource, WebserverWiring webserverWiring, ActiveMQConnectionFactory activeMQConnectionFactory) {
      this.dataSource = dataSource;
      this.webserverWiring = webserverWiring;
      this.activeMQConnectionFactory = activeMQConnectionFactory;
    }
  }

  public ApplicationWiring() {
    throw new AssertionError("Should not be instantiated outside of static factory method");
  }

  private ApplicationWiring(Singletons singletons, Settings settings, Logger applicationLogger) {
    this.singletons = singletons;
    this.settings = settings;
    this.applicationLogger = applicationLogger;
  }

  public static ApplicationWiring wiring(Settings settings, Logger applicationLogger) {
    JettyWebServer jettyWebServer = new JettyWebServer(applicationLogger, new Server(settings.webserverPort()));
    WebserverWiring webserverWiring = WebserverWiring.webserverWiring(jettyWebServer);
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(settings.brokerUrl());
    Singletons singletons = new Singletons(createDataSource(settings), webserverWiring, activeMQConnectionFactory);
    return new ApplicationWiring(singletons, settings, applicationLogger);
  }

  public DataSource getDataSource() {
    return singletons.dataSource;
  }

  ActiveMQConnectionFactory activeMQConnectionFactory() {
    return singletons.activeMQConnectionFactory;
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

  JmsExampleOneServlet jmsExampleOneServlet() {
    final MessageSender messageSender = new AuditMessageSender(new ActiveMqMessageSender(new JmsTemplate(activeMQConnectionFactory())), AUDIT_LOGGER);

    return new JmsExampleOneServlet(new UseCaseExampleOneStepOne(messageSender));
  }

  QueueConsumerConfiguration UseCaseExampleOneStepTwoInstructionListener() {
    MessageListener messageListener = new UseCaseExampleOneStepTwoInstructionListener(new UseCaseExampleOneStepTwo());
    UnaryOperator<MessageListener> messageListenerDecorator = aMessageListener -> new AuditMessageListener(aMessageListener, AUDIT_LOGGER);
    return new ApplicationQueueConsumerConfiguration(settings, applicationLogger, EXAMPLE_ONE_STEP_ONE_INSTRUCTION, messageListenerDecorator.apply(messageListener));
  }
}
