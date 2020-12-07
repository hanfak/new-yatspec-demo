package adapters.settings;

import adapters.jmsservice.QueueName;

import java.util.Optional;

public interface ActiveMQSettings {
    String brokerUrl();
    int getMaxConsumersFor(QueueName queueName);
    Optional<String> getActiveSiteForConsumer(QueueName queueName);
}
