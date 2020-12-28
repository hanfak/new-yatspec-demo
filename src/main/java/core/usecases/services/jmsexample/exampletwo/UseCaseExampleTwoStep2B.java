package core.usecases.services.jmsexample.exampletwo;

import core.usecases.ports.incoming.jmsexample.ExampleTwoStep2BService;
import org.slf4j.Logger;

public class UseCaseExampleTwoStep2B implements ExampleTwoStep2BService {

  private final Logger logger;

  public UseCaseExampleTwoStep2B(Logger logger) {
    this.logger = logger;
  }

  // Mapping for instruction via interface
  public void execute(ExampleTwoStep2BIncomingInstruction instruction) {
    logger.info("Step 2B: **** processing of message instruction ******");
    logger.info("Step 2B:  " + instruction.getValue());
    logger.info("Step 2B: **** Respond when finished ie some file/database/email/request to a user ******");
  }
}
