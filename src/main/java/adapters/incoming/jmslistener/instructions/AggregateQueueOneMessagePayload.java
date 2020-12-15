package adapters.incoming.jmslistener.instructions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AggregateQueueOneMessagePayload {

    private final String aggregateReference;

    @JsonCreator
    public AggregateQueueOneMessagePayload(
        @JsonProperty(value = "aggregateReference", required = true) String aggregateReference) {
        this.aggregateReference = aggregateReference;
    }

    public String getAggregateReference() {
        return aggregateReference;
    }
}
