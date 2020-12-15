package adapters.outgoing.jmssender;

import core.usecases.ports.outgoing.MessageService;
import org.slf4j.Logger;

import java.time.Duration;

import static adapters.jmsservice.QueueName.lookupQueue;
import static java.lang.String.format;

public class AuditMessageService implements MessageService {

  private final MessageService messageService;
  private final Logger logger;

  public AuditMessageService(MessageService messageService, Logger logger) {
    this.messageService = messageService;
    this.logger = logger;
  }

  @Override
  public void send(String queueName, String payload) {
    logger.info(logMessage(queueName, payload));
    messageService.send(queueName, payload);
  }

  @Override
  public void send(String queueName, String payload, Duration scheduledDelay) {
    String logMessage = format("%nOutbound message to queue: %s%n Payload:%n%s%nDelay: %d Minutes%n", lookupQueue(queueName).getActiveMQDestination(), payload, scheduledDelay.toMinutes());
    logger.info(logMessage);
    messageService.send(queueName, payload, scheduledDelay);
  }

  private String logMessage(String queueName, String payload) {
    return format("%nOutbound message to queue: %s%nMessageId: %nPayload:%n%s%n",
        lookupQueue(queueName).getActiveMQDestination().toString(), payload);
  }
}
