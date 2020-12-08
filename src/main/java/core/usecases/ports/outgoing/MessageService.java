package core.usecases.ports.outgoing;

public interface MessageService {
    void send(String queueName, String payload);
}
