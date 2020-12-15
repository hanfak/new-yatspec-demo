package adapters.incoming.jmslistener.queuelisteners;

import adapters.incoming.jmslistener.instructions.AggregateQueueOneMessagePayload;
import core.usecases.ports.incoming.AggregateExample1Step2Service;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static adapters.json.ObjectMapperFactory.createTolerantObjectMapper;
import static core.usecases.ports.incoming.AggregateExample1Step2Service.AggregateExample1Step2IncomingInstruction.incomingInstruction;

public class AggregateQueueOneInstructionListener implements MessageListener {

  private final AggregateExample1Step2Service service;

  public AggregateQueueOneInstructionListener(AggregateExample1Step2Service service) {
    this.service = service;
  }

  @Override
  public void onMessage(Message message) {
    try {
      String text = ((TextMessage) message).getText();
      AggregateQueueOneMessagePayload incomingMessage =
          createTolerantObjectMapper().readValue(text, AggregateQueueOneMessagePayload.class);
      service.execute(incomingInstruction(incomingMessage.getAggregateReference()));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
