package wiring;

import jmsservice.listener.ApplicationMessageListener;
import jmsservice.listener.AuditMessageListener;
import jmsservice.listener.configuration.ApplicationQueueConsumerConfiguration;
import jmsservice.listener.configuration.ConfigurableDefaultMessageListenerContainer;
import jmsservice.listener.configuration.QueueConsumerConfiguration;
import jmsservice.listener.queuelisteners.UseCaseExampleOneStepTwoInstructionListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import settings.Settings;

import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static jmsservice.QueueName.EXAMPLE_ONE_STEP_ONE_INSTRUCTION;

public class JmsWiring {

  private final List<ApplicationMessageListener> applicationMessageListeners = new ArrayList<>();

  private final UseCaseFactory useCaseFactory;
  private final Singletons singletons;
  private final Settings settings;
  private final Logger applicationLogger;
  private final Logger auditLogger;

  private static class Singletons {
    final ActiveMQConnectionFactory activeMQConnectionFactory;

    public Singletons(ActiveMQConnectionFactory activeMQConnectionFactory) {
      this.activeMQConnectionFactory = activeMQConnectionFactory;
    }
  }

  public JmsWiring() {
    throw new AssertionError("Should not be instantiated outside of static factory method");
  }

  public JmsWiring(UseCaseFactory useCaseFactory, Singletons singletons, Settings settings, Logger applicationLogger, Logger auditLogger) {
    this.useCaseFactory = useCaseFactory;
    this.singletons = singletons;
    this.settings = settings;
    this.applicationLogger = applicationLogger;
    this.auditLogger = auditLogger;
  }

  public static JmsWiring jmsWiring(UseCaseFactory useCaseFactory, Settings settings, Logger applicationLogger, Logger auditLogger) {
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(settings.brokerUrl());
    Singletons singletons = new Singletons(activeMQConnectionFactory);
    return new JmsWiring(useCaseFactory, singletons, settings, applicationLogger, auditLogger);
  }

  ActiveMQConnectionFactory activeMQConnectionFactory() {
    return singletons.activeMQConnectionFactory;
  }

  void startListeners() {
    applicationMessageListeners
        .forEach(ApplicationMessageListener::start);
  }

  void stopListeners() {
    applicationMessageListeners
        .forEach(ApplicationMessageListener::stop);
  }

  void addConsumerConfiguration() {
    applicationMessageListeners.clear();
    ConfigurableDefaultMessageListenerContainer defaultMessageListenerContainer = new ConfigurableDefaultMessageListenerContainer(activeMQConnectionFactory());
    applicationMessageListeners.add(new ApplicationMessageListener(useCaseExampleOneStepTwoInstructionListener(), defaultMessageListenerContainer));
  }

  QueueConsumerConfiguration useCaseExampleOneStepTwoInstructionListener() {
    MessageListener messageListener = new UseCaseExampleOneStepTwoInstructionListener(useCaseFactory.useCaseExampleOneStepTwo());
    UnaryOperator<MessageListener> messageListenerDecorator = aMessageListener -> new AuditMessageListener(aMessageListener, auditLogger);
    return new ApplicationQueueConsumerConfiguration(settings, applicationLogger, EXAMPLE_ONE_STEP_ONE_INSTRUCTION, messageListenerDecorator.apply(messageListener));
  }
}
