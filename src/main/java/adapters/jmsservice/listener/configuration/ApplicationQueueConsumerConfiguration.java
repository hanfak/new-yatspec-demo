package adapters.jmsservice.listener.configuration;

import adapters.jmsservice.QueueName;
import adapters.jmsservice.listener.ApplicationErrorHandler;
import adapters.settings.ActiveMQSettings;
import org.slf4j.Logger;
import org.springframework.util.ErrorHandler;

import javax.jms.MessageListener;

public class ApplicationQueueConsumerConfiguration implements QueueConsumerConfiguration {

    private final ActiveMQSettings activeMQSettings;
    private final Logger applicationLogger;
    private final QueueName queueName;
    private final MessageListener messageListener;

    public ApplicationQueueConsumerConfiguration(ActiveMQSettings activeMQSettings, Logger applicationLogger,
                                                 QueueName queueName, MessageListener messageListener) {
        this.activeMQSettings = activeMQSettings;
        this.applicationLogger = applicationLogger;
        this.queueName = queueName;
        this.messageListener = messageListener;
    }

    @Override
    public QueueName queueName() {
        return queueName;
    }

    @Override
    public MessageListener messageListener() {
        return messageListener;
    }

    @Override
    public ErrorHandler errorHandler() {
        return new ApplicationErrorHandler(applicationLogger);
    }

    @Override
    public int maxConsumers() {
        return activeMQSettings.getMaxConsumersFor(queueName());
    }
}
