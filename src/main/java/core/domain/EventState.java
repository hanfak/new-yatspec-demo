package core.domain;

public enum EventState {
  CREATED,
  SENT_FOR_PROCESSING,
  PROCESSING_EVENT,
  PROCESSED,
  IN_ERROR
}
