package core.usecases.ports.outgoing;

import adapters.jmsservice.QueueName;

public interface MessageSender {

    void send(QueueName queueName, String payload);
}
