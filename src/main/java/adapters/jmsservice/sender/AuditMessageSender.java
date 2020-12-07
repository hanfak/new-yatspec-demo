package adapters.jmsservice.sender;

import adapters.jmsservice.QueueName;
import core.usecases.ports.outgoing.MessageSender;
import org.slf4j.Logger;

import static java.lang.String.format;

public class AuditMessageSender implements MessageSender {

  private final MessageSender messageSender;
  private final Logger logger;

  public AuditMessageSender(MessageSender messageSender, Logger logger) {
    this.messageSender = messageSender;
    this.logger = logger;
  }

  @Override
  public void send(QueueName queueName, String payload) {
    String logMessage = format("%nOutbound message to queue: %s%nMessageId: %nPayload:%n%s%n", queueName.getActiveMQDestination().toString(), payload);
    logger.info(logMessage);
    messageSender.send(queueName, payload);
  }
}
