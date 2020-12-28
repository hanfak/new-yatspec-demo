package core.usecases.ports.incoming;

public interface ExternalCallExampleOneService {

  Output execute(String personId);

  class Output {
    private final String name;
    private final String birthYear;

    public Output(String name, String birthYear) {
      this.name = name;
      this.birthYear = birthYear;
    }

    public String getName() {
      return name;
    }

    public String getBirthYear() {
      return birthYear;
    }
  }
}
