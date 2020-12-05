package jmsservice.listener.configuration;

import jmsservice.QueueName;
import jmsservice.listener.SwitcherooErrorHandler;
import org.slf4j.Logger;
import org.springframework.util.ErrorHandler;
import settings.ActiveMQSettings;

import javax.jms.MessageListener;

public class SwitcherooQueueConsumerConfiguration implements QueueConsumerConfiguration {

    private final ActiveMQSettings activeMQSettings;
    private final Logger applicationLogger;
    private final QueueName queueName;
    private final MessageListener messageListener;

    public SwitcherooQueueConsumerConfiguration(ActiveMQSettings activeMQSettings, Logger applicationLogger,
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
        return new SwitcherooErrorHandler(applicationLogger);
    }

    @Override
    public int maxConsumers() {
        return activeMQSettings.getMaxConsumersFor(queueName());
    }
}
