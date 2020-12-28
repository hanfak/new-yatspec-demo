package adapters.incoming.jmslistener.queuelisteners;

import adapters.incoming.jmslistener.instructions.InternalQueueMessagePayload;
import core.usecases.ports.incoming.aggregateexample.AggregateExample1Step4CompletedService;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static adapters.json.ObjectMapperFactory.createTolerantObjectMapper;
import static core.usecases.ports.incoming.aggregateexample.AggregateExample1Step4CompletedService.AggregateExample1Step4CompletedIncomingInstruction.incomingInstruction;

public class InternalJobInstructionListener implements MessageListener {

  private final AggregateExample1Step4CompletedService service;

  public InternalJobInstructionListener(AggregateExample1Step4CompletedService service) {
    this.service = service;
  }

  @Override
  public void onMessage(Message message) {
    try {
      String text = ((TextMessage) message).getText();
      InternalQueueMessagePayload incomingMessage =
          createTolerantObjectMapper().readValue(text, InternalQueueMessagePayload.class);
      service.execute(incomingInstruction(incomingMessage.getAggregateReference()));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
