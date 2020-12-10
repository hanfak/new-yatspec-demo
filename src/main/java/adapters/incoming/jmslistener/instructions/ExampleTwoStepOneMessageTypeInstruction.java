package adapters.incoming.jmslistener.instructions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleTwoStepOneMessageTypeInstruction {

  private final String messageType;

  @JsonCreator
  public ExampleTwoStepOneMessageTypeInstruction(
      @JsonProperty(value = "messageType", required = true) String messageType) {
    this.messageType = messageType;
  }

  public String getMessageType() {
    return messageType;
  }
}