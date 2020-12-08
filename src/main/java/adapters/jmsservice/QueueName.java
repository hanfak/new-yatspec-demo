package adapters.jmsservice;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;

import java.util.Arrays;

// todo Should be split for listener and sender
public enum QueueName {

    EXAMPLE_ONE_STEP_ONE_QUEUE(new ActiveMQQueue("ExampleOneStepOneQueue"));

    private final ActiveMQDestination activeMQDestination;

    QueueName(ActiveMQDestination activeMQDestination) {
        this.activeMQDestination = activeMQDestination;
    }

    public ActiveMQDestination getActiveMQDestination() {
        return activeMQDestination;
    }

    public static QueueName lookupQueue(String queueName) {
        return Arrays.stream(QueueName.values())
            .filter(value -> value.activeMQDestination.getPhysicalName().equalsIgnoreCase(queueName))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
