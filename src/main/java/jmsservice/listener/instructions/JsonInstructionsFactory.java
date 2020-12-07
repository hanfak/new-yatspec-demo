package jmsservice.listener.instructions;

import usecases.jmsexample.InstructionFactory;

import static json.JsonUtils.jsonRepresentationOrBlowUpOf;

public class JsonInstructionsFactory implements InstructionFactory {
  @Override
  public String createUseCaseExampleOneStepTwoInstruction(Long id, String value) {
    return jsonRepresentationOrBlowUpOf(new UseCaseExampleOneStepTwoInstruction(id, value));
  }
}
