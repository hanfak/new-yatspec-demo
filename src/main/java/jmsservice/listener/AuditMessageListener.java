package jmsservice.listener;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static java.lang.String.format;
import static json.ObjectMapperFactory.createTolerantObjectMapper;


public class AuditMessageListener implements MessageListener {

    private final MessageListener messageListener;
    private final Logger auditLogger;

    public AuditMessageListener(MessageListener messageListener, Logger auditLogger) {
        this.messageListener = messageListener;
        this.auditLogger = auditLogger;
    }

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;

        try {
            String messageText = textMessage.getText();
            String prettyPayload = messageText;
            try {
                prettyPayload = createTolerantObjectMapper()
                        .enable(SerializationFeature.INDENT_OUTPUT)
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(createTolerantObjectMapper().readValue(messageText, Object.class));
            } catch (Exception ignored) { }

            String logMessage = format("%nIncoming message from queue: %s%nMessageId: %nPayload:%n%s%n", message.getJMSDestination().toString(), prettyPayload);
            auditLogger.info(logMessage);
        } catch (JMSException e) {
            throw new IllegalArgumentException(e);
        }

        messageListener.onMessage(message);
    }
}
