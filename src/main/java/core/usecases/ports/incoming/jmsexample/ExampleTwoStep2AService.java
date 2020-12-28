package core.usecases.ports.incoming.jmsexample;

import javax.validation.constraints.NotNull;

public interface ExampleTwoStep2AService {

  void execute(ExampleTwoStep2AIncomingInstruction instruction);

  final class ExampleTwoStep2AIncomingInstruction {
    @NotNull private final long jobId;
    @NotNull private final String value;

    private ExampleTwoStep2AIncomingInstruction(long jobId, String value) {
      this.jobId = jobId;
      this.value = value;
    }

    public static ExampleTwoStep2AIncomingInstruction incomingInstruction(long jobId, String value) {
      return new ExampleTwoStep2AIncomingInstruction(jobId, value);
    }

    public long getJobId() {
      return jobId;
    }

    public String getValue() {
      return value;
    }
  }
}
