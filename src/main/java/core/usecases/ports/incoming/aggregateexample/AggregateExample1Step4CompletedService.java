package core.usecases.ports.incoming.aggregateexample;

import javax.validation.constraints.NotNull;

public interface AggregateExample1Step4CompletedService {

  void execute(AggregateExample1Step4CompletedIncomingInstruction instruction);

  final class AggregateExample1Step4CompletedIncomingInstruction {

    @NotNull private final String aggregateReference;

    private AggregateExample1Step4CompletedIncomingInstruction(String aggregateReference) {
      this.aggregateReference = aggregateReference;
    }

    public static AggregateExample1Step4CompletedIncomingInstruction incomingInstruction(String aggregateReference) {
      return new AggregateExample1Step4CompletedIncomingInstruction(aggregateReference);
    }

    public String getAggregateReference() {
      return aggregateReference;
    }
  }
}
