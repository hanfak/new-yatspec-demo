package jmsservice.listener;

import usecases.UseCaseStepTwo;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static json.ObjectMapperFactory.createTolerantObjectMapper;

public class UseCaseStepTwoInstructionListener implements MessageListener {

    private final UseCaseStepTwo useCaseStepTwo;

    public UseCaseStepTwoInstructionListener(UseCaseStepTwo useCaseStepTwo) {
        this.useCaseStepTwo = useCaseStepTwo;
    }

    @Override
    public void onMessage(Message message) {
        final String text;
        try {
            text = ((TextMessage) message).getText();
            UseCaseStepTwoInstruction useCaseStepTwoInstruction = createTolerantObjectMapper().readValue(text, UseCaseStepTwoInstruction.class);
            // TODO: Mapping from json to usecase command object with validation in interface
            useCaseStepTwo.execute(useCaseStepTwoInstruction);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
