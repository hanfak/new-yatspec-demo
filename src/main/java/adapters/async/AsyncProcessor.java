package adapters.async;

public interface AsyncProcessor {
  void process(Runnable task);
}
