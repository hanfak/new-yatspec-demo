package adapters.incoming.webserver.servlets.aggregate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IncomingAggregateDTO {

  private final String aggregateReference;
  private final String aggregateData;

  @JsonCreator
  public IncomingAggregateDTO(
      @JsonProperty(value = "aggregateReference", required = true) String aggregateReference,
      @JsonProperty(value = "aggregateData", required = true) String aggregateData) {
    this.aggregateReference = aggregateReference;
    this.aggregateData = aggregateData;
  }

  public String getAggregateReference() {
    return aggregateReference;
  }

  public String getAggregateData() {
    return aggregateData;
  }
}
