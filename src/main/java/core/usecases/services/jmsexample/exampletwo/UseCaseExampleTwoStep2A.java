package core.usecases.services.jmsexample.exampletwo;

import core.usecases.ports.incoming.ExampleTwoStep2AService;
import org.slf4j.Logger;

public class UseCaseExampleTwoStep2A implements ExampleTwoStep2AService {

  private final Logger logger;

  public UseCaseExampleTwoStep2A(Logger logger) {
    this.logger = logger;
  }

  // Mapping for instruction via interface
  public void execute(ExampleTwoStep2AIncomingInstruction instruction) {
    logger.info("Step 2A: **** processing of message instruction ******");
    try {
      // Time out here will meean the processing will finish after the response from the http request is sent back to user
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      throw new IllegalArgumentException(e);
    }
    logger.info("Step 2A:  " + instruction.getValue());
    try {
      // Time out here will meean the processing will finish after the response from the http request is sent back to user
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      throw new IllegalArgumentException(e);
    }
    logger.info("Step 2A: **** Respond when finished ie some file/database/email/request to a user ******");
  }
}
