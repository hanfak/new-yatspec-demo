package usecases.generateresponseletter;

import javax.validation.constraints.NotNull;

public interface LetterCreator {

  void createLetter(UserLetterData data);

  // Could just be in domain package as only used in usecase layer
  final class UserLetterData {
    // Add validation here, does not pollute usecase code and creates an anti corruption layer
    // as there might be multiple callers from adapters (outer layers)
    // Could do it in the use case, if this object was not part of interface

    @NotNull private final String name;
    @NotNull private final String queryDetails;
    @NotNull private final String date;

    private UserLetterData(String name, String queryDetails, String date) {
      this.name = name;
      this.queryDetails = queryDetails;
      this.date = date;
    }

    public static UserLetterData userLetterData(String name, String queryDetails, String date) {
      validateDate(date);
      return new UserLetterData(name, queryDetails, date);
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
