package core.usecases.ports.outgoing;

import java.time.Duration;

public interface MessageService {
    void send(String queueName, String payload);
    void send(String queueName, String payload, Duration scheduledDelay);
}
