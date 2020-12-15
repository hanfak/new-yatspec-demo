package adapters.incoming.jmslistener.instructions;

import core.usecases.ports.outgoing.InstructionFactory;

import static adapters.json.JsonUtils.jsonRepresentationOrBlowUpOf;

public class JsonInstructionsFactory implements InstructionFactory {

  @Override
  public String createUseCaseExampleOneStepTwoInstruction(Long id, String value) {
    return jsonRepresentationOrBlowUpOf(new UseCaseExampleOneStepTwoMessagePayload(id, value));
  }

  @Override
  public String createUseCaseExampleTwoStep2AInstruction(Long id, String value) {
    return jsonRepresentationOrBlowUpOf(new UseCaseExampleTwoStep2AMessagePayload(id, value));
  }

  @Override
  public String createUseCaseExampleTwoStep2BInstruction(Long id, String value) {
    return jsonRepresentationOrBlowUpOf(new UseCaseExampleTwoStep2BMessagePayload(id, value));
  }

  @Override
  public String createAggregateQueueOneInstruction(String aggregateReference) {
    return jsonRepresentationOrBlowUpOf(
        new AggregateQueueOneMessagePayload(aggregateReference));
  }

  @Override
  public String createEventQueueOneInstruction(int eventId) {
    return jsonRepresentationOrBlowUpOf(
        new EventQueueOneMessagePayload(eventId));
  }

  @Override
  public String createInternalQueueInstruction(String aggregateReference) {
    return jsonRepresentationOrBlowUpOf(
        new InternalQueueMessagePayload(aggregateReference));
  }
}
