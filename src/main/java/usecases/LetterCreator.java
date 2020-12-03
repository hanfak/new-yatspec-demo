package usecases;

import com.google.common.util.concurrent.UncheckedExecutionException;
import fileservice.FileReader;
import fileservice.FileWriter;
import fileservice.FilenameService;
import fileservice.TemplateReplacementFileService;
import org.slf4j.Logger;
import webserver.servlets.ReadFileUpdaterDTO;

public class LetterCreator {

  private final TemplateReplacementFileService templateReplacementFileService;
  private final FileReader<String, String> fileReader;
  private final FileWriter fileWriter;
  private final FilenameService filenameService;
  private final Logger logger;

  public LetterCreator(TemplateReplacementFileService templateReplacementFileService, FileReader<String, String> fileReader, FileWriter fileWriter, FilenameService filenameService, Logger logger) {
    this.templateReplacementFileService = templateReplacementFileService;
    this.fileReader = fileReader;
    this.fileWriter = fileWriter;
    this.filenameService = filenameService;
    this.logger = logger;
  }

  public void createLetter(ReadFileUpdaterDTO dto) {
    logger.info("Generating file in separate thread");
    String template = fileReader.readFile("target/classes/files/template.txt");
    TemplateReplacementFileService.TemplateData templateData = new TemplateReplacementFileService.TemplateData(dto.getName(), dto.getQueryDetails(), dto.getDate());
    String responseLetterUpdated = templateReplacementFileService.replacePlaceHolder(template, templateData);
    fileWriter.write(() -> filenameService.createNameSpecificFilename("responseLetter.txt", templateData.getName()), responseLetterUpdated); //TODO path should be properties
    // Just to show async example
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new UncheckedExecutionException(e);
    }
    logger.info("Generating file has finished");
  }
}
