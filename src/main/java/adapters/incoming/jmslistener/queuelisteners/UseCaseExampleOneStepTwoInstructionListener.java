package adapters.incoming.jmslistener.queuelisteners;

import adapters.incoming.jmslistener.instructions.UseCaseExampleOneStepTwoInstruction;
import core.usecases.services.jmsexample.UseCaseExampleOneStepTwo;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static adapters.json.ObjectMapperFactory.createTolerantObjectMapper;

public class UseCaseExampleOneStepTwoInstructionListener implements MessageListener {

    private final UseCaseExampleOneStepTwo useCaseExampleOneStepTwo;

    public UseCaseExampleOneStepTwoInstructionListener(UseCaseExampleOneStepTwo useCaseExampleOneStepTwo) {
        this.useCaseExampleOneStepTwo = useCaseExampleOneStepTwo;
    }

    @Override
    public void onMessage(Message message) {
        final String text;
        try {
            text = ((TextMessage) message).getText();
            UseCaseExampleOneStepTwoInstruction useCaseExampleOneStepTwoInstruction = createTolerantObjectMapper().readValue(text, UseCaseExampleOneStepTwoInstruction.class);
            // TODO: Mapping from json to usecase command object with validation in interface
            useCaseExampleOneStepTwo.execute(useCaseExampleOneStepTwoInstruction);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
