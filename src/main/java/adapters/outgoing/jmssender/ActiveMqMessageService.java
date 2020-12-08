package adapters.outgoing.jmssender;

import core.usecases.ports.outgoing.MessageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.core.JmsTemplate;

import static adapters.jmsservice.QueueName.lookupQueue;
import static javax.jms.DeliveryMode.PERSISTENT;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class ActiveMqMessageService implements MessageService {

    private final JmsTemplate jmsTemplate;

    public ActiveMqMessageService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void send(String queueName, String payload) {
        jmsTemplate.setSessionAcknowledgeMode(AUTO_ACKNOWLEDGE);
        jmsTemplate.setDeliveryMode(PERSISTENT);

        try {
            ActiveMQTextMessage activeMQTextMessage = new ActiveMQTextMessage();
            activeMQTextMessage.setText(payload);

            jmsTemplate.convertAndSend(lookupQueue(queueName).getActiveMQDestination(), activeMQTextMessage);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
