package usecases;

import jmsservice.listener.UseCaseStepTwoInstruction;

public class UseCaseStepTwo {

  public void execute(UseCaseStepTwoInstruction instruction) {
    // Get info from starwars api
    //    Person characterInfo = starWarsInterfaceService.getCharacterInfo(instruction.getValue());

    // write file with json "{\"Description\": \"%s was born on %s\"}"
    // or some consumer action

  }
}
