package adapters.outgoing.jmssender;

import adapters.jmsservice.QueueName;
import core.usecases.ports.outgoing.MessageSender;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.core.JmsTemplate;

import static javax.jms.DeliveryMode.PERSISTENT;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class ActiveMqMessageSender implements MessageSender {

    private final JmsTemplate jmsTemplate;

    public ActiveMqMessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void send(QueueName queueName, String payload) {
        jmsTemplate.setSessionAcknowledgeMode(AUTO_ACKNOWLEDGE);
        jmsTemplate.setDeliveryMode(PERSISTENT);

        try {
            ActiveMQTextMessage activeMQTextMessage = new ActiveMQTextMessage();
            activeMQTextMessage.setText(payload);

            jmsTemplate.convertAndSend(queueName.getActiveMQDestination(), activeMQTextMessage);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
