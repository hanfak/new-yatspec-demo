package core.domain;

public enum AggregateState {
  CREATED,
  SENT_FOR_PROCESSING,
  PROCESSING_AGGREGATE,
  COMPLETED,
  COMPLETED_WITH_ERROR
}
