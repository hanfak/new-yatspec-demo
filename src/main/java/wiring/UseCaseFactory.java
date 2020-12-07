package wiring;

import fileservice.FileSystemFileReader;
import fileservice.FileWriter;
import fileservice.InMemoryIdService;
import org.slf4j.Logger;
import settings.Settings;
import usecases.generateresponseletter.GenerateResponseLetterUseCase;
import usecases.generateresponseletter.GenerateResponseLetterUseCasePort;
import usecases.generateresponseletter.ResponseLetterReplacer;
import usecases.jmsexample.UseCaseExampleOneStepTwo;

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
