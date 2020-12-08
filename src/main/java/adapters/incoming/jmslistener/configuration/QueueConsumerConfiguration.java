package adapters.incoming.jmslistener.configuration;

import adapters.jmsservice.QueueName;
import org.springframework.util.ErrorHandler;

import javax.jms.MessageListener;

public interface QueueConsumerConfiguration {
  int maxConsumers();

  ErrorHandler errorHandler();

  QueueName queueName();

  MessageListener messageListener();
}
