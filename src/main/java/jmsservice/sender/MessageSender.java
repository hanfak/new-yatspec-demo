package jmsservice.sender;

import jmsservice.QueueName;

public interface MessageSender {

    void send(QueueName queueName, String payload);
}
