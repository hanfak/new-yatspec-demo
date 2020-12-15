package core.usecases.ports.incoming;

import javax.validation.constraints.NotNull;

public interface AggregateExample1Step2Service {

  void execute(AggregateExample1Step2IncomingInstruction instruction);

  final class AggregateExample1Step2IncomingInstruction {

    @NotNull private final String aggregateReference;

    private AggregateExample1Step2IncomingInstruction(String aggregateReference) {
      this.aggregateReference = aggregateReference;
    }

    public static AggregateExample1Step2IncomingInstruction incomingInstruction(String aggregateReference) {
      return new AggregateExample1Step2IncomingInstruction(aggregateReference);
    }

    public String getAggregateReference() {
      return aggregateReference;
    }
  }
}
