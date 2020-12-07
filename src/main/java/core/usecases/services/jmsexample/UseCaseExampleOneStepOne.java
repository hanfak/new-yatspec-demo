package core.usecases.services.jmsexample;

import com.google.common.util.concurrent.UncheckedExecutionException;
import core.usecases.ports.incoming.UseCase;
import core.usecases.ports.outgoing.InstructionFactory;
import core.usecases.ports.outgoing.MessageSender;
import org.slf4j.Logger;

import static adapters.jmsservice.QueueName.EXAMPLE_ONE_STEP_ONE_INSTRUCTION;

// Called from web request from servlet
public class UseCaseExampleOneStepOne implements UseCase {

  private final MessageSender messageSender;
  private final InstructionFactory instructionFactory;
  private final Logger logger;

  public UseCaseExampleOneStepOne(MessageSender messageSender, InstructionFactory instructionFactory, Logger applicationLogger) {
    this.messageSender = messageSender;
    this.instructionFactory = instructionFactory;
    this.logger = applicationLogger;
  }

  @Override
  public void execute() {
    // Can do some preprocessing first
    logger.info("**** Preprocessing ******");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      throw new UncheckedExecutionException(e);
    }

    // tODO extract out queue name, maybe add to instruction factory
    messageSender.send(EXAMPLE_ONE_STEP_ONE_INSTRUCTION, instructionFactory.createUseCaseExampleOneStepTwoInstruction(1L, "blah"));
    logger.info("**** Message sent ******");
  }
}
