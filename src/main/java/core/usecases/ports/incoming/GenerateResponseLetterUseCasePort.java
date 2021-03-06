package core.usecases.ports.incoming;

import javax.validation.constraints.NotNull;

@FunctionalInterface
public interface GenerateResponseLetterUseCasePort {

  void createLetter(GenerateResponseLetterCommand command);

  // Could just be in domain package as only used in usecase layer
  final class GenerateResponseLetterCommand {
    // Add validation here, does not pollute usecase code and creates an anti corruption layer
    // as there might be multiple callers from adapters (outer layers)
    // Could do it in the use case, if this object was not part of interface

    @NotNull private final String name;
    @NotNull private final String queryDetails;
    @NotNull private final String date;

    private GenerateResponseLetterCommand(String name, String queryDetails, String date) {
      this.name = name;
      this.queryDetails = queryDetails;
      this.date = date;
    }

    public static GenerateResponseLetterCommand userLetterData(String name, String queryDetails, String date) {
      validateDate(date);
      return new GenerateResponseLetterCommand(name, queryDetails, date);
    }

    private static void validateDate(String date) {
      // can have other validation here
//      if (date.matches("[A-Za-z]")) {
//        throw new IllegalArgumentException("data not in correct format");
//      }
    }

    public String getName() {
      return name;
    }

    public String getQueryDetails() {
      return queryDetails;
    }

    public String getDate() {
      return date;
    }
  }
}
