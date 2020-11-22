package httpclient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;

public class HttpRequestBodySubscriber<T> implements Flow.Subscriber<T> {

  private final CountDownLatch latch = new CountDownLatch(1);
  private T body;

  public T getBodyItems() {
    try {
      this.latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return body;
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    subscription.request(Long.MAX_VALUE);
  }

  @Override
  public void onNext(T item) {
    this.body = item;
  }

  @Override
  public void onError(Throwable throwable) {
    this.latch.countDown();
  }

  @Override
  public void onComplete() {
    this.latch.countDown();
  }
}