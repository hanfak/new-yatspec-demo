package usecases.generateresponseletter;

import com.google.common.util.concurrent.UncheckedExecutionException;
import fileservice.FileReader;
import fileservice.FileWriter;
import fileservice.UniqueIdService;
import org.slf4j.Logger;
import settings.ResponseLetterSettings;
import usecases.generateresponseletter.TemplateReplacementFileService.TemplateData;

import static java.lang.String.format;
import static java.nio.file.Paths.get;

public class PersonalisedLetterCreatorUseCase implements LetterCreator {

  private final TemplateReplacementFileService templateReplacementFileService;
  private final FileReader<String, String> fileReader;
  private final FileWriter fileWriter;
  private final UniqueIdService uniqueIdService;
  private final ResponseLetterSettings settings;
  private final Logger logger;

  public PersonalisedLetterCreatorUseCase(TemplateReplacementFileService templateReplacementFileService,
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
  public void createLetter(UserLetterData data) {
    logger.info("Generating file in separate thread");

    // TODO: new usecase/flow Add business logic, database call, change return value to result(??)
    // if(nameService.getnames.contains(data.getName()) {
    //    create letter

    // TODO: refactor out to outer layer ??
    // This can be moved to an outer layer as it is mainly file io, but does contain business logic (what template to use, data etc
    String template = fileReader.readFile(settings.responseLetterTemplatePath());
    String responseLetterUpdated = templateReplacementFileService.replacePlaceHolder(template, buildTemplateData(data));
    String letterName = format(settings.responseLetterFilenameTemplate(), data.getName());
    fileWriter.write(
        () -> get(letterName),
        responseLetterUpdated);

    someExtraProcessing();    // Just to show async example

    logger.info("Generating file has finished");
  }

  private TemplateData buildTemplateData(UserLetterData data) {
    return new TemplateData(data.getName(), data.getQueryDetails(), data.getDate(), uniqueIdService.execute());
  }

  private void someExtraProcessing() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new UncheckedExecutionException(e);
    }
  }
}
