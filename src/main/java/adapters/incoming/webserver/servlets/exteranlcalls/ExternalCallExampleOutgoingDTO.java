package adapters.incoming.webserver.servlets.exteranlcalls;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalCallExampleOutgoingDTO {

  private final String personId;
  private final Details details;

  @JsonCreator
  public ExternalCallExampleOutgoingDTO(
      @JsonProperty(value = "personId", required = true) String personId,
      @JsonProperty(value = "details", required = true) Details details
  ) {
    this.personId = personId;
    this.details = details;
  }

  public String getPersonId() {
    return personId;
  }

  public Details getDetails() {
    return details;
  }

  static class Details {

    private final String name;
    private final String birthYear;

    @JsonCreator
    Details(@JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "birthYear", required = true) String birthYear) {
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
