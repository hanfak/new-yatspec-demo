package usecases.jmsexample;

import com.google.common.util.concurrent.UncheckedExecutionException;
import jmsservice.listener.instructions.UseCaseExampleOneStepTwoInstruction;

public class UseCaseExampleOneStepTwo {

  public void execute(UseCaseExampleOneStepTwoInstruction instruction) {
    // Get info from starwars api
    //    Person characterInfo = starWarsInterfaceService.getCharacterInfo(instruction.getValue());
    System.out.println("**** processing of message instruction ******");
    System.out.println(instruction.getValue());

    // write file with json "{\"Description\": \"%s was born on %s\"}"
    // or some consumer action
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new UncheckedExecutionException(e);
    }

    System.out.println("**** Respond when finished ie some file/database/email/request to a user ******");


  }
}
