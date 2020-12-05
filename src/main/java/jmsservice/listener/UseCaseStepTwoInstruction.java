package jmsservice.listener;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UseCaseStepTwoInstruction {

    private final long jobId;
    private final String value;

    @JsonCreator
    public UseCaseStepTwoInstruction(
            @JsonProperty(value = "jobId", required = true) long jobId,
            @JsonProperty(value = "value", required = true) String value) {
        this.jobId = jobId;
        this.value = value;
    }
}
