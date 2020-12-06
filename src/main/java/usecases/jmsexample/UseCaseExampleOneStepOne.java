package usecases.jmsexample;

import jmsservice.listener.instructions.UseCaseExampleOneStepTwoInstruction;
import jmsservice.sender.MessageSender;

import static jmsservice.QueueName.EXAMPLE_ONE_STEP_ONE_INSTRUCTION;
import static json.JsonUtils.jsonRepresentationOrBlowUpOf;

// Called from web request from servlet
public class UseCaseExampleOneStepOne {

  private final MessageSender messageSender;

  public UseCaseExampleOneStepOne(MessageSender messageSender) {
    this.messageSender = messageSender;
  }

  public void execute() {
    // Can do some preprocessing first
    System.out.println("**** Preprocessing ******");
    // Pass message using data from database to queue
    messageSender.send(EXAMPLE_ONE_STEP_ONE_INSTRUCTION, createInstruction());
    System.out.println("**** Message sent ******");

  }

  private String createInstruction() {
    return jsonRepresentationOrBlowUpOf(new UseCaseExampleOneStepTwoInstruction(1L, "Blah"));
  }
}
