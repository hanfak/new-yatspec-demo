package core.usecases.ports.outgoing;

import java.time.Duration;

public interface InternalJobSettings {
  Duration getAggregateCompleteDelayDuration();
}
