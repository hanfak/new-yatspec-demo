package usecases;

import jmsservice.listener.UseCaseStepTwoInstruction;
import jmsservice.sender.MessageSender;

import static jmsservice.QueueName.STEP_ONE_INSTRUCTION;
import static json.JsonUtils.jsonRepresentationOrBlowUpOf;

// Called from web request from servlet
public class UseCaseStepOne {

  private final MessageSender messageSender;

  public UseCaseStepOne(MessageSender messageSender) {
    this.messageSender = messageSender;
  }

  public void execute() {
    // Get from database

    // Pass message using data from database to queue
    messageSender.send(STEP_ONE_INSTRUCTION, createInstruction());
  }

  private String createInstruction() {
    return jsonRepresentationOrBlowUpOf(new UseCaseStepTwoInstruction(1L, "Blah"));
  }
}
