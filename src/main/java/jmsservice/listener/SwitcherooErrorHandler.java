package jmsservice.listener;

import org.slf4j.Logger;
import org.springframework.util.ErrorHandler;

public class SwitcherooErrorHandler implements ErrorHandler {

    private final Logger logger;

    public SwitcherooErrorHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handleError(Throwable t) {
        //TODO: retry/send to dead letter queue??
        logger.error("Exception caught from JMS Transaction; Implement this class properly when there is a business requirement to do:\n", t);
    }
}
