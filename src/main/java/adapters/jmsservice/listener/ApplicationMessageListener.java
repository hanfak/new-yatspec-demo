package adapters.jmsservice.listener;

import adapters.jmsservice.listener.configuration.ConfigurableDefaultMessageListenerContainer;
import adapters.jmsservice.listener.configuration.QueueConsumerConfiguration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

public class ApplicationMessageListener {

    private final DefaultMessageListenerContainer messageListenerContainer;
    private final QueueConsumerConfiguration queueConsumerConfiguration;

    public ApplicationMessageListener(QueueConsumerConfiguration queueConsumerConfiguration, ConfigurableDefaultMessageListenerContainer messageListenerContainer) {
        this.queueConsumerConfiguration = queueConsumerConfiguration;
        this.messageListenerContainer = messageListenerContainer.create(queueConsumerConfiguration);
    }

    public void start() {
        messageListenerContainer.initialize();
        messageListenerContainer.start();
    }

    public void stop() {
        messageListenerContainer.shutdown();
        messageListenerContainer.stop();
    }

    public QueueConsumerConfiguration consumerConfiguration() {
        return queueConsumerConfiguration;
    }
}
