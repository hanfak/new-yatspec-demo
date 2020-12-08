package adapters.incoming.jmslistener.instructions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UseCaseExampleOneStepTwoMessagePayload {

    private final long jobId;
    private final String value;

    @JsonCreator
    public UseCaseExampleOneStepTwoMessagePayload(
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
}
