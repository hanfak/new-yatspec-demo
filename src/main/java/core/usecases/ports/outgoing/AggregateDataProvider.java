package core.usecases.ports.outgoing;

import java.util.Optional;

public interface AggregateDataProvider {
  void createAggregate(String aggregateJobReference, String aggregateState, String aggregateData);
  void updateAggregateState(String aggregateJobReference, String aggregateState);
  Optional<String> findAggregateData(String aggregateJobReference);
}
