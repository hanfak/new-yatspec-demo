package wiring;

import adapters.incoming.jmslistener.instructions.JsonInstructionsFactory;
import adapters.incoming.webserver.servlets.DataProvider;
import adapters.libraries.ApacheCommonsLang3Adapter;
import adapters.outgoing.databaseservice.AggregateDataProviderRepository;
import adapters.outgoing.databaseservice.EventDataProviderRepository;
import adapters.outgoing.fileservice.AggregateFileReport;
import adapters.outgoing.fileservice.FileSystemFileReader;
import adapters.outgoing.fileservice.FileSystemWriter;
import adapters.outgoing.fileservice.InMemoryIdService;
import adapters.outgoing.jmssender.ActiveMqMessageService;
import adapters.outgoing.jmssender.AuditMessageService;
import adapters.settings.internal.Settings;
import core.usecases.TransientDependencyUsecase;
import core.usecases.ports.incoming.GenerateResponseLetterUseCasePort;
import core.usecases.ports.incoming.aggregateexample.AggregateExample1Step1Service;
import core.usecases.ports.incoming.aggregateexample.AggregateExample1Step2Service;
import core.usecases.ports.incoming.aggregateexample.AggregateExample1Step3Service;
import core.usecases.ports.incoming.aggregateexample.AggregateExample1Step4CompletedService;
import core.usecases.ports.incoming.jmsexample.ExampleOneStepTwoService;
import core.usecases.ports.incoming.jmsexample.ExampleTwoStep2AService;
import core.usecases.ports.incoming.jmsexample.ExampleTwoStep2BService;
import core.usecases.ports.outgoing.MessageService;
import core.usecases.services.aggregateexample.exampleone.UseCaseAggregateExample1Step1;
import core.usecases.services.aggregateexample.exampleone.UseCaseAggregateExample1Step2;
import core.usecases.services.aggregateexample.exampleone.UseCaseAggregateExample1Step3;
import core.usecases.services.aggregateexample.exampleone.UseCaseAggregateExample1Step4Completed;
import core.usecases.services.externalcalls.ExternalCallExampleOneUsecase;
import core.usecases.services.generateresponseletter.GenerateResponseLetterUseCase;
import core.usecases.services.generateresponseletter.ResponseLetterReplacer;
import core.usecases.services.jmsexample.exampleone.UseCaseExampleOneStepOne;
import core.usecases.services.jmsexample.exampleone.UseCaseExampleOneStepTwo;
import core.usecases.services.jmsexample.exampletwo.UseCaseExampleTwoStep2A;
import core.usecases.services.jmsexample.exampletwo.UseCaseExampleTwoStep2B;
import core.usecases.services.jmsexample.exampletwo.UseCaseExampleTwoStepOne;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.springframework.jms.core.JmsTemplate;

import java.util.function.Supplier;

public class UseCaseFactory {

  private final Logger logger;
  private final Settings settings;
  private final MessageService messageService;
  private final ExternalCallWiringInterface externalCallWiring;
  private final AggregateDataProviderRepository aggregateDataProviderRepository;
  private final EventDataProviderRepository eventDataProviderRepository;
  private final FileSystemWriter fileWriter;
  private final Supplier<DataProvider> dataProviderSupplier;

  private UseCaseFactory(Logger logger, Settings settings, MessageService messageService,
                         ExternalCallWiringInterface externalCallWiring, AggregateDataProviderRepository aggregateDataProviderRepository,
                         EventDataProviderRepository eventDataProviderRepository, FileSystemWriter fileWriter,
                         Supplier<DataProvider> dataProviderSupplier) {
    this.logger = logger;
    this.settings = settings;
    this.messageService = messageService;
    this.externalCallWiring = externalCallWiring;
    this.aggregateDataProviderRepository = aggregateDataProviderRepository;
    this.eventDataProviderRepository = eventDataProviderRepository;
    this.fileWriter = fileWriter;
    this.dataProviderSupplier = dataProviderSupplier;
    // Instead of injecting one supplier, can inject factory of suppliers,
    // and let the factory methods below ask for the supplier they need to pass into the usecase
  }

  public static UseCaseFactory useCaseFactory(FileIoFactory fileIoFactory, DataRespositoryFactoryInterface dataRepositoryFactory, Logger logger, ExternalCallWiringInterface externalCallWiring, Settings settings, ActiveMQConnectionFactory activeMQConnectionFactory, Logger auditLogger, Supplier<DataProvider> dataProviderSupplier) {
    MessageService messageService = new AuditMessageService(new ActiveMqMessageService(new JmsTemplate(activeMQConnectionFactory)), auditLogger);
    return new UseCaseFactory(logger, settings, messageService, externalCallWiring, dataRepositoryFactory.aggregateDataProviderRepository(), dataRepositoryFactory.eventDataProviderRepository(), fileIoFactory.fileWriter(), dataProviderSupplier);
  }

  // TODO make singleton
  private JsonInstructionsFactory jsonInstructionFactory() {
    return new JsonInstructionsFactory();
  }

  UseCaseExampleOneStepOne useCaseExampleOneStepOne() {
    return new UseCaseExampleOneStepOne(messageService,
        jsonInstructionFactory(),
        logger);
  }


  ExampleTwoStep2AService useCaseExampleTwoStep2A() {
    return new UseCaseExampleTwoStep2A(logger);
  }

  ExampleTwoStep2BService useCaseExampleTwoStep2B() {
    return new UseCaseExampleTwoStep2B(logger);
  }

  UseCaseExampleTwoStepOne useCaseExampleTwoStepOne() {
    return new UseCaseExampleTwoStepOne(messageService,
        jsonInstructionFactory(),
        logger);
  }

  ExampleOneStepTwoService useCaseExampleOneStepTwo() {
    return new UseCaseExampleOneStepTwo(logger);
  }

  AggregateExample1Step1Service aggregateExample1Step1Service() {
    return new UseCaseAggregateExample1Step1(
        aggregateDataProviderRepository,
        messageService,
        jsonInstructionFactory(),
        settings,
        logger
    );
  }

  AggregateExample1Step2Service aggregateExample1Step2Service() {
    return new UseCaseAggregateExample1Step2(
        aggregateDataProviderRepository,
        eventDataProviderRepository,
        messageService,
        jsonInstructionFactory(),
        logger
    );
  }

  AggregateExample1Step3Service aggregateExample1Step3Service() {
    return new UseCaseAggregateExample1Step3(
        eventDataProviderRepository
    );
  }

  AggregateExample1Step4CompletedService aggregateExample1Step4CompletedService() {
    return new UseCaseAggregateExample1Step4Completed(
        aggregateDataProviderRepository,
        eventDataProviderRepository,
        messageService,
        jsonInstructionFactory(),
        new AggregateFileReport(fileWriter), settings, logger
    );
  }

  GenerateResponseLetterUseCasePort generateResponseLetterUseCase() {
    ResponseLetterReplacer templateReplacementFileService = new ResponseLetterReplacer(new ApacheCommonsLang3Adapter());
    return new GenerateResponseLetterUseCase(
        templateReplacementFileService,
        new FileSystemFileReader(),
        fileWriter,
        new InMemoryIdService(), settings, logger);
  }

  ExternalCallExampleOneUsecase externalCallExampleOneService() {
    return new ExternalCallExampleOneUsecase(externalCallWiring.starWarsInterfaceService());
  }

  TransientDependencyUsecase transientDependencyUsecase() {
    return new TransientDependencyUsecase(dataProviderSupplier);
  }
}
