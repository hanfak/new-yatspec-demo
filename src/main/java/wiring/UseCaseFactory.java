package wiring;

import adapters.libraries.ApacheCommonsLang3Adapter;
import adapters.outgoing.fileservice.FileSystemFileReader;
import adapters.outgoing.fileservice.InMemoryIdService;
import adapters.settings.internal.Settings;
import core.usecases.ports.incoming.ExampleOneStepTwoService;
import core.usecases.ports.incoming.ExampleTwoStep2AService;
import core.usecases.ports.incoming.ExampleTwoStep2BService;
import core.usecases.ports.incoming.GenerateResponseLetterUseCasePort;
import core.usecases.ports.outgoing.FileWriter;
import core.usecases.services.generateresponseletter.GenerateResponseLetterUseCase;
import core.usecases.services.generateresponseletter.ResponseLetterReplacer;
import core.usecases.services.jmsexample.exampleone.UseCaseExampleOneStepTwo;
import core.usecases.services.jmsexample.exampletwo.UseCaseExampleTwoStep2A;
import core.usecases.services.jmsexample.exampletwo.UseCaseExampleTwoStep2B;
import org.slf4j.Logger;

public class UseCaseFactory {

  private final Logger logger;
  private final Settings settings;

  public UseCaseFactory(Logger logger, Settings settings) {
    this.logger = logger;
    this.settings = settings;
  }

  ExampleTwoStep2AService useCaseExampleTwoStep2A() {
    return new UseCaseExampleTwoStep2A(logger);
  }

  ExampleTwoStep2BService useCaseExampleTwoStep2B() {
    return new UseCaseExampleTwoStep2B(logger);
  }

  ExampleOneStepTwoService useCaseExampleOneStepTwo() {
    return new UseCaseExampleOneStepTwo(logger);
  }

  GenerateResponseLetterUseCasePort generateResponseLetterUseCase(FileWriter fileWriter) {
    ResponseLetterReplacer templateReplacementFileService = new ResponseLetterReplacer(new ApacheCommonsLang3Adapter());
    return new GenerateResponseLetterUseCase(
        templateReplacementFileService,
        new FileSystemFileReader(),
        fileWriter,
        new InMemoryIdService(), settings, logger);
  }
}
