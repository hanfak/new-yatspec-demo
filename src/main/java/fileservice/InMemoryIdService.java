package fileservice;

import java.util.concurrent.atomic.AtomicLong;

public class InMemoryIdService implements UniqueIdService{

  private static final AtomicLong counter = new AtomicLong(1L);

  @Override
  public long execute() {
    return counter.getAndIncrement();
  }
}
