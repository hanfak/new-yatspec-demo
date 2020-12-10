package adapters.incoming.jmslistener.instructions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = "messageType")
public class UseCaseExampleTwoStep2AMessagePayload {

    private final String messageType = "step2APayload";
    private final long jobId;
    private final String value;

    @JsonCreator
    public UseCaseExampleTwoStep2AMessagePayload(
            @JsonProperty(value = "jobId", required = true) long jobId,
            @JsonProperty(value = "value", required = true) String value) {
        this.jobId = jobId;
        this.value = value;
    }

    public long getJobId() {
        return jobId;
    }

    public String getValue() {
        return value;
    }

    public String getMessageType() {
        return messageType;
    }
}
