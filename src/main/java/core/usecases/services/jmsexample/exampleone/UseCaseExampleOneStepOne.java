package core.usecases.services.jmsexample.exampleone;

import core.usecases.ports.incoming.UseCase;
import core.usecases.ports.outgoing.InstructionFactory;
import core.usecases.ports.outgoing.MessageService;
import org.slf4j.Logger;

// Called from web request from servlet
public class UseCaseExampleOneStepOne implements UseCase {

  private static final String LOCATION = "ExampleOneStepOneQueue"; // TODO some mapping, or individual adapter per queue

  private final MessageService messageService;
  private final InstructionFactory instructionFactory;
  private final Logger logger;

  public UseCaseExampleOneStepOne(MessageService messageService, InstructionFactory instructionFactory, Logger applicationLogger) {
    this.messageService = messageService;
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
      throw new IllegalStateException(e);
    }

    messageService.send(LOCATION, instructionFactory.createUseCaseExampleOneStepTwoInstruction(1L, "blah"));

    logger.info("**** Message sent ******");
  }
}
