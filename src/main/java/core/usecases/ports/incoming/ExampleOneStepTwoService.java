package core.usecases.ports.incoming;

import javax.validation.constraints.NotNull;

public interface ExampleOneStepTwoService {

  void execute(ExampleOneStepTwoIncomingInstruction instruction);

  final class ExampleOneStepTwoIncomingInstruction {
    @NotNull private final long jobId;
    @NotNull private final String value;

    private ExampleOneStepTwoIncomingInstruction(long jobId, String value) {
      this.jobId = jobId;
      this.value = value;
    }

    public static ExampleOneStepTwoIncomingInstruction incomingInstruction(long jobId, String value) {
      return new ExampleOneStepTwoIncomingInstruction(jobId, value);
    }

    public long getJobId() {
      return jobId;
    }

    public String getValue() {
      return value;
    }
  }
}
