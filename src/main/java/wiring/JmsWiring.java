package wiring;

import adapters.incoming.jmslistener.ApplicationMessageListener;
import adapters.incoming.jmslistener.AuditMessageListener;
import adapters.incoming.jmslistener.configuration.ApplicationQueueConsumerConfiguration;
import adapters.incoming.jmslistener.configuration.ConfigurableDefaultMessageListenerContainer;
import adapters.incoming.jmslistener.configuration.QueueConsumerConfiguration;
import adapters.incoming.jmslistener.queuelisteners.*;
import adapters.settings.internal.Settings;
import core.usecases.ports.incoming.AggregateExample1Step2Service;
import core.usecases.ports.incoming.AggregateExample1Step3Service;
import core.usecases.ports.incoming.AggregateExample1Step4CompletedService;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;

import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static adapters.jmsservice.QueueName.*;

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

  public static JmsWiring jmsWiring(UseCaseFactory useCaseFactory, ActiveMQConnectionFactory activeMQConnectionFactory, Settings settings, Logger applicationLogger, Logger auditLogger) {
    Singletons singletons = new Singletons(activeMQConnectionFactory);
    return new JmsWiring(useCaseFactory, singletons, settings, applicationLogger, auditLogger);
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
    ConfigurableDefaultMessageListenerContainer defaultMessageListenerContainer = new ConfigurableDefaultMessageListenerContainer(singletons.activeMQConnectionFactory);
    applicationMessageListeners.add(new ApplicationMessageListener(useCaseExampleOneStepTwoInstructionListener(), defaultMessageListenerContainer));

    applicationMessageListeners.add(new ApplicationMessageListener(useCaseExampleTwoStep2InstructionListener(), defaultMessageListenerContainer));
    applicationMessageListeners.add(new ApplicationMessageListener(useCaseExampleTwoStep2InstructionListener(), defaultMessageListenerContainer));     // Need a duplicate listener for queue that you want to have multiple consumers on

    applicationMessageListeners.add(new ApplicationMessageListener(aggregateQueueOneInstructionListener(), defaultMessageListenerContainer));
    applicationMessageListeners.add(new ApplicationMessageListener(eventQueueOneInstructionListener(), defaultMessageListenerContainer));
    applicationMessageListeners.add(new ApplicationMessageListener(internalJobQueueInstructionListener(), defaultMessageListenerContainer));
  }

  QueueConsumerConfiguration useCaseExampleOneStepTwoInstructionListener() {
    MessageListener messageListener = new UseCaseExampleOneStepTwoInstructionListener(useCaseFactory.useCaseExampleOneStepTwo());
    UnaryOperator<MessageListener> messageListenerDecorator = aMessageListener -> new AuditMessageListener(aMessageListener, auditLogger);
    return new ApplicationQueueConsumerConfiguration(settings, applicationLogger, EXAMPLE_ONE_STEP_ONE_QUEUE, messageListenerDecorator.apply(messageListener));
  }

  QueueConsumerConfiguration useCaseExampleTwoStep2InstructionListener() {
    MessageListener messageListener = new UseCaseExampleTwoStep2InstructionListener(useCaseFactory.useCaseExampleTwoStep2A(), useCaseFactory.useCaseExampleTwoStep2B(), applicationLogger);
    UnaryOperator<MessageListener> messageListenerDecorator = aMessageListener -> new AuditMessageListener(aMessageListener, auditLogger);
    return new ApplicationQueueConsumerConfiguration(settings, applicationLogger, EXAMPLE_TWO_STEP_ONE_QUEUE, messageListenerDecorator.apply(messageListener));
  }

  QueueConsumerConfiguration aggregateQueueOneInstructionListener() {
    AggregateExample1Step2Service service = useCaseFactory.aggregateExample1Step2Service();
    MessageListener messageListener = new AggregateQueueOneInstructionListener(service);
    UnaryOperator<MessageListener> messageListenerDecorator = aMessageListener -> new AuditMessageListener(aMessageListener, auditLogger);
    return new ApplicationQueueConsumerConfiguration(settings, applicationLogger, AGGREGATE_QUEUE_ONE, messageListenerDecorator.apply(messageListener));
  }

  QueueConsumerConfiguration eventQueueOneInstructionListener() {
    AggregateExample1Step3Service service = useCaseFactory.aggregateExample1Step3Service();
    MessageListener messageListener = new EventQueueOneInstructionListener(service);
    UnaryOperator<MessageListener> messageListenerDecorator = aMessageListener -> new AuditMessageListener(aMessageListener, auditLogger);
    return new ApplicationQueueConsumerConfiguration(settings, applicationLogger, EVENT_QUEUE_ONE, messageListenerDecorator.apply(messageListener));
  }

  QueueConsumerConfiguration internalJobQueueInstructionListener() {
    AggregateExample1Step4CompletedService service = useCaseFactory.aggregateExample1Step4CompletedService();
    MessageListener messageListener = new InternalJobInstructionListener(service);
    UnaryOperator<MessageListener> messageListenerDecorator = aMessageListener -> new AuditMessageListener(aMessageListener, auditLogger);
    return new ApplicationQueueConsumerConfiguration(settings, applicationLogger, INTERNAL_JOB_QUEUE, messageListenerDecorator.apply(messageListener));
  }
}
