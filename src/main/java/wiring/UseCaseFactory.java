package wiring;

import adapters.fileservice.FileSystemFileReader;
import adapters.fileservice.InMemoryIdService;
import adapters.settings.Settings;
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

  UseCaseExampleOneStepTwo useCaseExampleOneStepTwo() {
    return new UseCaseExampleOneStepTwo(logger);
  }

  GenerateResponseLetterUseCasePort generateResponseLetterUseCase(FileWriter fileWriter) {
    return new GenerateResponseLetterUseCase(new ResponseLetterReplacer(), new FileSystemFileReader(),
        fileWriter, new InMemoryIdService(), settings, logger);
  }
}
