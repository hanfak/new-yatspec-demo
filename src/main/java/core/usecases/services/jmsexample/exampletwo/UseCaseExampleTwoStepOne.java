package core.usecases.services.jmsexample.exampletwo;

import adapters.incoming.jmslistener.instructions.JsonInstructionsFactory;
import core.usecases.ports.incoming.UseCase;
import core.usecases.ports.outgoing.MessageService;
import org.slf4j.Logger;

public class UseCaseExampleTwoStepOne implements UseCase {

  private static final String LOCATION = "ExampleTwoStepOneQueue"; 
  
  private final MessageService messageService;
  private final JsonInstructionsFactory instructionFactory;
  private final Logger logger;

  public UseCaseExampleTwoStepOne(MessageService messageService, JsonInstructionsFactory instructionFactory, Logger logger) {
    this.messageService = messageService;
    this.instructionFactory = instructionFactory;
    this.logger = logger;
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

    // Could use some logic to say which message goes to which queue
    // TODO: this object must have message queue
    // As  the message is being picked up by this app, it will do first message, then the second message
    messageService.send(LOCATION, instructionFactory.createUseCaseExampleTwoStep2AInstruction(1L, "blah"));
    // if this app had replicas, then it will pick up the messages on two different processes, but second message will finish
    //    processing first, due to the time delay set in UseCaseExampleTwoStep2B is less than in UseCaseExampleTwoStep2A
    messageService.send(LOCATION, instructionFactory.createUseCaseExampleTwoStep2BInstruction(1L, "cheese"));

    logger.info("**** Message sent ******");
  }
}
