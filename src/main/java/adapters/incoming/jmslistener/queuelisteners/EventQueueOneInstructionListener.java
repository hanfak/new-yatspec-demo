package adapters.incoming.jmslistener.queuelisteners;

import adapters.incoming.jmslistener.instructions.EventQueueOneMessagePayload;
import core.usecases.ports.incoming.AggregateExample1Step3Service;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static adapters.json.ObjectMapperFactory.createTolerantObjectMapper;
import static core.usecases.ports.incoming.AggregateExample1Step3Service.EventIncomingInstruction.incomingInstruction;

public class EventQueueOneInstructionListener implements MessageListener {

  private final AggregateExample1Step3Service service;

  public EventQueueOneInstructionListener(AggregateExample1Step3Service service) {
    this.service = service;
  }

  @Override
  public void onMessage(Message message) {
    try {
      String text = ((TextMessage) message).getText();
      EventQueueOneMessagePayload incomingMessage =
          createTolerantObjectMapper().readValue(text, EventQueueOneMessagePayload.class);
      service.execute(incomingInstruction(incomingMessage.getEventId()));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
