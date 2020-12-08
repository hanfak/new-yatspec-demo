package adapters.outgoing.jmssender;

import core.usecases.ports.outgoing.MessageService;
import org.slf4j.Logger;

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

  private String logMessage(String queueName, String payload) {
    return format("%nOutbound message to queue: %s%nMessageId: %nPayload:%n%s%n",
        lookupQueue(queueName).getActiveMQDestination().toString(), payload);
  }
}
