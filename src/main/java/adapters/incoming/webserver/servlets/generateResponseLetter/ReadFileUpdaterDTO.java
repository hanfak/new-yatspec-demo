package adapters.incoming.webserver.servlets.generateResponseLetter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReadFileUpdaterDTO {

  private final String name;
  private final String queryDetails;
  private final String date;

  @JsonCreator
  ReadFileUpdaterDTO(@JsonProperty(value = "name", required = true) String name,
                            @JsonProperty(value = "queryDetails", required = true)String queryDetails,
                            @JsonProperty(value = "date", required = true) String date) {
    this.name = name;
    this.queryDetails = queryDetails;
    this.date = date;
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
