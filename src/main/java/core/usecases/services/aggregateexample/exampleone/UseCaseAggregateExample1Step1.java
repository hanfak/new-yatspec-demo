package core.usecases.services.aggregateexample.exampleone;

import core.usecases.ports.incoming.AggregateExample1Step1Service;
import core.usecases.ports.outgoing.AggregateDataProvider;
import core.usecases.ports.outgoing.InstructionFactory;
import core.usecases.ports.outgoing.InternalJobSettings;
import core.usecases.ports.outgoing.MessageService;
import org.slf4j.Logger;

import static core.domain.AggregateState.CREATED;
import static core.domain.AggregateState.SENT_FOR_PROCESSING;

public class UseCaseAggregateExample1Step1 implements AggregateExample1Step1Service {

  private static final String LOCATION = "AggregateQueueOne";
  private static final String INTERNAL_JOB_QUEUE = "InternalJobQueue";

  private final AggregateDataProvider aggregateDataProvider;
  private final MessageService messageService;
  private final InstructionFactory instructionFactory;
  private final InternalJobSettings settings;
  private final Logger logger;

  public UseCaseAggregateExample1Step1(AggregateDataProvider aggregateDataProvider, MessageService messageService, InstructionFactory instructionFactory, InternalJobSettings settings, Logger logger) {
    this.aggregateDataProvider = aggregateDataProvider;
    this.messageService = messageService;
    this.instructionFactory = instructionFactory;
    this.settings = settings;
    this.logger = logger;
  }
  // No need to capture error, as will be sent back via http response, logged
  // run when receive http request
  @Override
  public void execute(AggregateCommand command) {
    // create aggregate with CREATED state in db
    aggregateDataProvider.createAggregate(command.getAggregateReference(), CREATED.name(),command.getAggregateData());

    // send message with aggRef to queue
    sendMessage(command, LOCATION);

    // update aggregate with SENT_FOR_PROCESSING in db
    aggregateDataProvider.updateAggregateState(command.getAggregateReference(), SENT_FOR_PROCESSING.name());
    // send message to internal queue which sends message every x minutes to check if the aggregate has completed processing
    sendToInternalJobQueue(command);
  }

  private void sendToInternalJobQueue(AggregateCommand command) {
    messageService.send(INTERNAL_JOB_QUEUE,
        instructionFactory.createInternalQueueInstruction(
            command.getAggregateReference()), settings.getAggregateCompleteDelayDuration());
  }

  private void sendMessage(AggregateCommand command, String location) {
    // This is a design decision, do we want to send the data in message, or use ref in message in next use case to get the aggData from database
    messageService.send(location,
        instructionFactory.createAggregateQueueOneInstruction(
            command.getAggregateReference()));
    // can send command, but command type is tied to interface and used only by usecase or callers of usecase
    //    Can extract the command out of interface to it's own class to solve this
    //    I think show what specific data is sent in message is useful
  }
}
