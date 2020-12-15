package wiring;

import adapters.incoming.jmslistener.instructions.JsonInstructionsFactory;
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
import core.usecases.ports.incoming.*;
import core.usecases.ports.outgoing.MessageService;
import core.usecases.services.aggregateexample.exampleone.UseCaseAggregateExample1Step1;
import core.usecases.services.aggregateexample.exampleone.UseCaseAggregateExample1Step2;
import core.usecases.services.aggregateexample.exampleone.UseCaseAggregateExample1Step3;
import core.usecases.services.aggregateexample.exampleone.UseCaseAggregateExample1Step4Completed;
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

public class UseCaseFactory {

  private final Logger logger;
  private final Settings settings;
  private final Singletons singletons;

  private static class Singletons {

    private final MessageService messageService;
    private final AggregateDataProviderRepository aggregateDataProviderRepository;
    private final EventDataProviderRepository eventDataProviderRepository;
    public final FileSystemWriter fileWriter;

    private Singletons(MessageService messageService, AggregateDataProviderRepository aggregateDataProviderRepository, EventDataProviderRepository eventDataProviderRepository, FileSystemWriter fileWriter) {
      this.messageService = messageService;
      this.aggregateDataProviderRepository = aggregateDataProviderRepository;
      this.eventDataProviderRepository = eventDataProviderRepository;
      this.fileWriter = fileWriter;
    }
  }

  private UseCaseFactory(Logger logger, Settings settings, Singletons singletons) {
    this.logger = logger;
    this.settings = settings;
    this.singletons = singletons;
  }

  public static UseCaseFactory useCaseFactory(FileIoFactory fileIoFactory, DataRepositoryFactory dataRepositoryFactory, Logger logger, Settings settings, ActiveMQConnectionFactory activeMQConnectionFactory, Logger auditLogger) {
    MessageService messageService = new AuditMessageService(new ActiveMqMessageService(new JmsTemplate(activeMQConnectionFactory)), auditLogger);
    Singletons singletons = new Singletons(messageService, dataRepositoryFactory.aggregateDataProviderRepository(), dataRepositoryFactory.eventDataProviderRepository(), fileIoFactory.fileWriter());
    return new UseCaseFactory(logger, settings, singletons);
  }

  // TODO make singleton
  private JsonInstructionsFactory jsonInstructionFactory() {
    return new JsonInstructionsFactory();
  }

  UseCaseExampleOneStepOne useCaseExampleOneStepOne() {
    return new UseCaseExampleOneStepOne(singletons.messageService,
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
    return new UseCaseExampleTwoStepOne(singletons.messageService,
        jsonInstructionFactory(),
        logger);
  }

  ExampleOneStepTwoService useCaseExampleOneStepTwo() {
    return new UseCaseExampleOneStepTwo(logger);
  }

  AggregateExample1Step1Service aggregateExample1Step1Service() {
    return new UseCaseAggregateExample1Step1(
        singletons.aggregateDataProviderRepository,
        singletons.messageService,
        jsonInstructionFactory(),
        settings,
        logger
        );
  }

  AggregateExample1Step2Service aggregateExample1Step2Service() {
    return new UseCaseAggregateExample1Step2(
        singletons.aggregateDataProviderRepository,
        singletons.eventDataProviderRepository,
        singletons.messageService,
        jsonInstructionFactory(),
        logger
    );
  }

  AggregateExample1Step3Service aggregateExample1Step3Service() {
    return new UseCaseAggregateExample1Step3(
        singletons.eventDataProviderRepository
    );
  }

  AggregateExample1Step4CompletedService aggregateExample1Step4CompletedService() {
    return new UseCaseAggregateExample1Step4Completed(
        singletons.aggregateDataProviderRepository,
        singletons.eventDataProviderRepository,
        singletons.messageService,
        jsonInstructionFactory(),
        new AggregateFileReport(singletons.fileWriter), settings, logger
    );
  }

  GenerateResponseLetterUseCasePort generateResponseLetterUseCase() {
    ResponseLetterReplacer templateReplacementFileService = new ResponseLetterReplacer(new ApacheCommonsLang3Adapter());
    return new GenerateResponseLetterUseCase(
        templateReplacementFileService,
        new FileSystemFileReader(),
        singletons.fileWriter,
        new InMemoryIdService(), settings, logger);
  }
}
