package adapters.incoming.jmslistener.queuelisteners;

import adapters.incoming.jmslistener.instructions.ExampleTwoStepOneMessageTypeInstruction;
import adapters.incoming.jmslistener.instructions.UseCaseExampleTwoStep2AMessagePayload;
import adapters.incoming.jmslistener.instructions.UseCaseExampleTwoStep2BMessagePayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.usecases.ports.incoming.ExampleTwoStep2AService;
import core.usecases.ports.incoming.ExampleTwoStep2AService.ExampleTwoStep2AIncomingInstruction;
import core.usecases.ports.incoming.ExampleTwoStep2BService;
import core.usecases.ports.incoming.ExampleTwoStep2BService.ExampleTwoStep2BIncomingInstruction;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static adapters.json.ObjectMapperFactory.createTolerantObjectMapper;

public class UseCaseExampleTwoStep2InstructionListener implements MessageListener {

  private final ExampleTwoStep2AService exampleTwoStep2AService;
  private final ExampleTwoStep2BService exampleTwoStep2BService;

  public UseCaseExampleTwoStep2InstructionListener(ExampleTwoStep2AService exampleTwoStep2AService, ExampleTwoStep2BService exampleTwoStep2BService) {
    this.exampleTwoStep2AService = exampleTwoStep2AService;
    this.exampleTwoStep2BService = exampleTwoStep2BService;
  }

  @Override
  public void onMessage(Message message) {
    ObjectMapper tolerantObjectMapper = createTolerantObjectMapper();
    try {
      String text = ((TextMessage) message).getText();
      // Here we just extract the common field in either message
      ExampleTwoStepOneMessageTypeInstruction messageType = tolerantObjectMapper.readValue(text, ExampleTwoStepOneMessageTypeInstruction.class);
      // Depending on the type of message in the payload, it will unmarshall the specific message and execute it's relevant usecase
      if (messageType.getMessageType().equalsIgnoreCase("step2APayload")) {
        executeExampleTwoStep2AService(text);
      } else if (messageType.getMessageType().equalsIgnoreCase("step2BPayload")) {
        executeExampleTwoStep2BService(text);
      } else {
        // No message matched a usecase, it will get dropped
        throw new IllegalArgumentException("Payload did not match");
      }
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private void executeExampleTwoStep2BService(String text) throws java.io.IOException {
    UseCaseExampleTwoStep2BMessagePayload incomingMessage =
        createTolerantObjectMapper().readValue(text, UseCaseExampleTwoStep2BMessagePayload.class);
    ExampleTwoStep2BIncomingInstruction instruction = ExampleTwoStep2BIncomingInstruction.incomingInstruction(incomingMessage.getJobId(), incomingMessage.getValue());
    exampleTwoStep2BService.execute(instruction);
  }

  private void executeExampleTwoStep2AService(String text) throws java.io.IOException {
    UseCaseExampleTwoStep2AMessagePayload incomingMessage =
        createTolerantObjectMapper().readValue(text, UseCaseExampleTwoStep2AMessagePayload.class);
    ExampleTwoStep2AIncomingInstruction instruction = ExampleTwoStep2AIncomingInstruction.incomingInstruction(incomingMessage.getJobId(), incomingMessage.getValue());
    exampleTwoStep2AService.execute(instruction);
  }
}
