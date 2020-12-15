package core.usecases.ports.incoming;

import javax.validation.constraints.NotNull;

public interface AggregateExample1Step1Service {

  void execute(AggregateCommand command);

  final class AggregateCommand {
    @NotNull private final String aggregateReference;
    @NotNull private final String aggregateData;

    public AggregateCommand(String aggregateReference, String aggregateData) {
      this.aggregateReference = aggregateReference;
      this.aggregateData = aggregateData;
    }

    public String getAggregateReference() {
      return aggregateReference;
    }

    public String getAggregateData() {
      return aggregateData;
    }
  }
}
