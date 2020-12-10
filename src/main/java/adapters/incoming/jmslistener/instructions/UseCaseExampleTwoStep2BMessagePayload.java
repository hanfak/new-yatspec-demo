package adapters.incoming.jmslistener.instructions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = "messageType")
public class UseCaseExampleTwoStep2BMessagePayload {

    private final String messageType = "step2BPayload";
    private final long jobId;
    private final String value;

    @JsonCreator
    public UseCaseExampleTwoStep2BMessagePayload(
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
