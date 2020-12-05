package jmsservice.listener.configuration;

import jmsservice.QueueName;
import org.springframework.util.ErrorHandler;

import javax.jms.MessageListener;

public interface QueueConsumerConfiguration {
  int maxConsumers();

  ErrorHandler errorHandler();

  QueueName queueName();

  MessageListener messageListener();
}
