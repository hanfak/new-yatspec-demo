package async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceAsyncProcessor implements AsyncProcessor {
  @Override
  public void process(Runnable task) {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.execute(task);
  }
}
