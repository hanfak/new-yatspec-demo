package core.usecases.ports.outgoing;

public interface InstructionFactory {
  String createUseCaseExampleOneStepTwoInstruction(Long id, String value);

  String createUseCaseExampleTwoStep2AInstruction(Long id, String value);
  String createUseCaseExampleTwoStep2BInstruction(Long id, String value);

  String createAggregateQueueOneInstruction(String aggregateReference);
  String createEventQueueOneInstruction(int eventId);

  String createInternalQueueInstruction(String aggregateReference);
}
