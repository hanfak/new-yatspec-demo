package usecases.jmsexample;

import com.google.common.util.concurrent.UncheckedExecutionException;
import jmsservice.listener.instructions.UseCaseExampleOneStepTwoInstruction;
import org.slf4j.Logger;

public class UseCaseExampleOneStepTwo {

  private final Logger logger;

  public UseCaseExampleOneStepTwo(Logger logger) {
    this.logger = logger;
  }

  public void execute(UseCaseExampleOneStepTwoInstruction instruction) {
    // Get info from starwars api
    //    Person characterInfo = starWarsInterfaceService.getCharacterInfo(instruction.getValue());
    logger.info("**** processing of message instruction ******");
    logger.info(instruction.getValue());

    // write file with json "{\"Description\": \"%s was born on %s\"}"
    // or some consumer action
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new UncheckedExecutionException(e);
    }

    logger.info("**** Respond when finished ie some file/database/email/request to a user ******");
  }
}
