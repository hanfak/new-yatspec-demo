package adapters.incoming.jmslistener.instructions;

import core.usecases.ports.outgoing.InstructionFactory;

import static adapters.json.JsonUtils.jsonRepresentationOrBlowUpOf;

public class JsonInstructionsFactory implements InstructionFactory {
  @Override
  public String createUseCaseExampleOneStepTwoInstruction(Long id, String value) {
    return jsonRepresentationOrBlowUpOf(new UseCaseExampleOneStepTwoMessagePayload(id, value));
  }
}
