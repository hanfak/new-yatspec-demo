package adapters.outgoing.fileservice;

import adapters.logging.LoggingCategory;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import core.domain.Person;
import core.domain.Species;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;


public class MyFileService implements FileService {

  private static final String DIRECTORY_OF_LOCATION_OF_CREATED_FILES = "target/test-classes/testFiles/created";
  private static final String FILE_NAME = "/data-%d.xml";

  private final InMemoryIdService uniqueIdService;
  private final XmlMapper xmlMapper;
  private final Logger logger;

  public MyFileService(InMemoryIdService uniqueIdService, XmlMapper xmlMapper, Logger logger) {
    this.uniqueIdService = uniqueIdService;
    this.xmlMapper = xmlMapper;
    this.logger = logger;
  }

  @Override
  public void storeData(String personId, Person characterInfo, Species speciesInfo) {
    xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

    try {
      Files.createDirectories(Paths.get(DIRECTORY_OF_LOCATION_OF_CREATED_FILES));
      CharacterDetails data = new CharacterDetails(
          personId,
          characterInfo.getName(),
          characterInfo.getBirthYear(),
          speciesInfo.getAverageHeight(),
          speciesInfo.getLifeSpan(),
          speciesInfo.getName());
      String s = ReflectionToStringBuilder.toString(data, new MultilineRecursiveToStringStyle());
      logger.info("file contents written to file: " + s);
      xmlMapper.writeValue(new FileOutputStream(
          format(DIRECTORY_OF_LOCATION_OF_CREATED_FILES + FILE_NAME,
              uniqueIdService.execute())), data);
    } catch (IOException e) {
      logger.error("error processing file", e);
    }
  }

  @Override
  public CharacterDetails readData(String filename) throws IOException {
    Path fileLocation = Paths.get("target/classes/files/" + filename);
    xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    CharacterDetails characterDetails = xmlMapper.readValue(Files.newInputStream(fileLocation), CharacterDetails.class);
    String s = ReflectionToStringBuilder.toString(characterDetails, new MultilineRecursiveToStringStyle());
    System.out.println("s = " + s);
    return characterDetails;
  }

  public static void main(String... args) throws IOException {
    FileService fileService = new MyFileService(new InMemoryIdService(), new XmlMapper(), getLogger(LoggingCategory.APPLICATION.name()));
    fileService.readData("example.xml");
  }
}
