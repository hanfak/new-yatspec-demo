package core.usecases.services.generateresponseletter;

import core.usecases.ports.incoming.GenerateResponseLetterUseCasePort;
import core.usecases.ports.outgoing.*;
import core.usecases.ports.outgoing.TemplateReplacementFileService.TemplateData;
import org.slf4j.Logger;

import static java.lang.String.format;
import static java.nio.file.Paths.get;

// slf4j is 3rd party, and should be be wrapped in interface, but it is already an interface
// and there is an acceptable and conscious decision to allow it in the Core package

public class GenerateResponseLetterUseCase implements GenerateResponseLetterUseCasePort {

  private final TemplateReplacementFileService templateReplacementFileService;
  private final FileReader<String, String> fileReader;
  private final FileWriter fileWriter;
  private final UniqueIdService uniqueIdService;
  private final ResponseLetterSettings settings;
  private final Logger logger;

  public GenerateResponseLetterUseCase(TemplateReplacementFileService templateReplacementFileService,
                                       FileReader<String, String> fileReader,
                                       FileWriter fileWriter,
                                       UniqueIdService uniqueIdService,
                                       ResponseLetterSettings settings,
                                       Logger logger) {
    this.templateReplacementFileService = templateReplacementFileService;
    this.fileReader = fileReader;
    this.fileWriter = fileWriter;
    this.uniqueIdService = uniqueIdService;
    this.settings = settings;
    this.logger = logger;
  }

  @Override
  public void createLetter(GenerateResponseLetterCommand command) {
    logger.info("Generating file in separate thread");

    // TODO: new usecase/flow Add business logic, database call, change return value to result(??)
    // if(nameService.getnames.contains(command.getName()) {
    //    create letter

    // TODO: refactor out to outer layer ??
    // This can be moved to an outer layer as it is mainly file io, but does contain business logic (what template to use, command etc
    String template = fileReader.readFile(settings.responseLetterTemplatePath());
    String responseLetterUpdated = templateReplacementFileService.replacePlaceHolder(template, buildTemplateData(command));
    String letterName = format(settings.responseLetterFilenameTemplate(), command.getName());
    fileWriter.write(
        () -> get(letterName),
        responseLetterUpdated);

    someExtraProcessing();    // Just to show adapters.async example

    logger.info("Generating file has finished");
  }

  private TemplateData buildTemplateData(GenerateResponseLetterCommand command) {
    return new TemplateData(command.getName(), command.getQueryDetails(), command.getDate(), uniqueIdService.execute());
  }

  private void someExtraProcessing() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }
}
