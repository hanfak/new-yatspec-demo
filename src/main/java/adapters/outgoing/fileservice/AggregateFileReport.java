package adapters.outgoing.fileservice;

import core.usecases.ports.outgoing.AggregateReport;

import java.nio.file.Paths;

import static adapters.json.JsonUtils.jsonRepresentationOrBlowUpOf;

public class AggregateFileReport implements AggregateReport {

  private final FileSystemWriter fileSystemWriter;

  public AggregateFileReport(FileSystemWriter fileSystemWriter) {
    this.fileSystemWriter = fileSystemWriter;
  }

  public void generate(AggregateCompletionDetails details) {
    // Create json, use jackson with details
    AggregateCompletionDetailsJacksonDTO aggregateCompletionDetailsJacksonDTO = new AggregateCompletionDetailsJacksonDTO(
        details.getAggregateReference(),
        details.getAggregateState(),
        details.getProcessedEvents(),
        details.getSuccessfulEvents(),
        details.getNumberOfInErrorEvents(),
        details.getFailedEvents());

    // write json string in file
    String reportInJsonFormat = jsonRepresentationOrBlowUpOf(aggregateCompletionDetailsJacksonDTO);
    fileSystemWriter.write(() -> Paths.get("aggregateReport.txt"), reportInJsonFormat);
  }
}
