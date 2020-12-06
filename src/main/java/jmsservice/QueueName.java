package jmsservice;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;

public enum QueueName {

    EXAMPLE_ONE_STEP_ONE_INSTRUCTION(new ActiveMQQueue("ExampleOneStepOneInstruction"));

    private final ActiveMQDestination activeMQDestination;

    QueueName(ActiveMQDestination activeMQDestination) {
        this.activeMQDestination = activeMQDestination;
    }

    public ActiveMQDestination getActiveMQDestination() {
        return activeMQDestination;
    }
}
