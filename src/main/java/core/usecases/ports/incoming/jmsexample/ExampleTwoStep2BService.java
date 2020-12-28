package core.usecases.ports.incoming.jmsexample;

import javax.validation.constraints.NotNull;

public interface ExampleTwoStep2BService {

  void execute(ExampleTwoStep2BIncomingInstruction instruction);

  final class ExampleTwoStep2BIncomingInstruction {
    @NotNull private final long jobId;
    @NotNull private final String value;

    private ExampleTwoStep2BIncomingInstruction(long jobId, String value) {
      this.jobId = jobId;
      this.value = value;
    }

    public static ExampleTwoStep2BIncomingInstruction incomingInstruction(long jobId, String value) {
      return new ExampleTwoStep2BIncomingInstruction(jobId, value);
    }

    public long getJobId() {
      return jobId;
    }

    public String getValue() {
      return value;
    }
  }
}
