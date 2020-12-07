package usecases.jmsexample;

import com.google.common.util.concurrent.UncheckedExecutionException;
import jmsservice.sender.MessageSender;
import org.slf4j.Logger;

import static jmsservice.QueueName.EXAMPLE_ONE_STEP_ONE_INSTRUCTION;

// Called from web request from servlet
public class UseCaseExampleOneStepOne {

  private final MessageSender messageSender;
  private final InstructionFactory instructionFactory;
  private final Logger logger;

  public UseCaseExampleOneStepOne(MessageSender messageSender, InstructionFactory instructionFactory, Logger applicationLogger) {
    this.messageSender = messageSender;
    this.instructionFactory = instructionFactory;
    this.logger = applicationLogger;
  }

  public void execute() {
    // Can do some preprocessing first
    logger.info("**** Preprocessing ******");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      throw new UncheckedExecutionException(e);
    }

    messageSender.send(EXAMPLE_ONE_STEP_ONE_INSTRUCTION, instructionFactory.createUseCaseExampleOneStepTwoInstruction(1L, "blah"));
    logger.info("**** Message sent ******");
  }
}
