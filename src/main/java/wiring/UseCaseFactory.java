package wiring;

import adapters.libraries.ApacheCommonsLang3Adapter;
import adapters.outgoing.fileservice.FileSystemFileReader;
import adapters.outgoing.fileservice.InMemoryIdService;
import adapters.settings.internal.Settings;
import core.usecases.ports.incoming.ExampleOneStepTwoService;
import core.usecases.ports.incoming.GenerateResponseLetterUseCasePort;
import core.usecases.ports.outgoing.FileWriter;
import core.usecases.services.generateresponseletter.GenerateResponseLetterUseCase;
import core.usecases.services.generateresponseletter.ResponseLetterReplacer;
import core.usecases.services.jmsexample.UseCaseExampleOneStepTwo;
import org.slf4j.Logger;

public class UseCaseFactory {

  private final Logger logger;
  private final Settings settings;

  public UseCaseFactory(Logger logger, Settings settings) {
    this.logger = logger;
    this.settings = settings;
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
