package jmsservice.listener.configuration;

import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static org.springframework.jms.listener.DefaultMessageListenerContainer.CACHE_CONSUMER;

public class ConfigurableDefaultMessageListenerContainer {

    private final ConnectionFactory connectionFactory;

    public ConfigurableDefaultMessageListenerContainer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public DefaultMessageListenerContainer create(QueueConsumerConfiguration queueConsumerConfiguration) {
        DefaultMessageListenerContainer receiver = new DefaultMessageListenerContainer();
        receiver.setMaxConcurrentConsumers(queueConsumerConfiguration.maxConsumers());
        receiver.setErrorHandler(queueConsumerConfiguration.errorHandler());
        receiver.setConnectionFactory(connectionFactory);
        receiver.setDestination(queueConsumerConfiguration.queueName().getActiveMQDestination());
        receiver.setSessionAcknowledgeMode(AUTO_ACKNOWLEDGE);
        receiver.setCacheLevel(CACHE_CONSUMER);
        receiver.setMessageListener(queueConsumerConfiguration.messageListener());
        return receiver;
    }
}
