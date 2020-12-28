package adapters.incoming.jmslistener.queuelisteners;

import adapters.incoming.jmslistener.instructions.UseCaseExampleOneStepTwoMessagePayload;
import core.usecases.ports.incoming.jmsexample.ExampleOneStepTwoService;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static adapters.json.ObjectMapperFactory.createTolerantObjectMapper;
import static core.usecases.ports.incoming.jmsexample.ExampleOneStepTwoService.ExampleOneStepTwoIncomingInstruction.incomingInstruction;

public class UseCaseExampleOneStepTwoInstructionListener implements MessageListener {

  private final ExampleOneStepTwoService exampleOneStepTwoService;

  public UseCaseExampleOneStepTwoInstructionListener(ExampleOneStepTwoService exampleOneStepTwoService) {
    this.exampleOneStepTwoService = exampleOneStepTwoService;
  }

  @Override
  public void onMessage(Message message) {
    try {
      String text = ((TextMessage) message).getText();
      UseCaseExampleOneStepTwoMessagePayload incomingMessage =
          createTolerantObjectMapper().readValue(text, UseCaseExampleOneStepTwoMessagePayload.class);
      exampleOneStepTwoService.execute(incomingInstruction(incomingMessage.getJobId(), incomingMessage.getValue()));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
