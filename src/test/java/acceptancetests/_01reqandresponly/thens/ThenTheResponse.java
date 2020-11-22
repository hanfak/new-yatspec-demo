package acceptancetests._01reqandresponly.thens;

import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.mockito.internal.util.Supplier;

import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenTheResponse {

  private final Supplier<HttpResponse<String>> responseSupplier;

  public ThenTheResponse(Supplier<HttpResponse<String>> responseSupplier) {
    this.responseSupplier = responseSupplier;
  }

  public AbstractStringAssert<?> hasBody() {
    return assertThat(response().body());
  }

  public AbstractIntegerAssert<?> hasStatusCode() {
    return assertThat(response().statusCode());
  }

  public AbstractStringAssert<?> hasContentType() {
    String headValueOfContentType = response().headers()
        .firstValue("content-type")
        .orElse("not_found");
    return assertThat(headValueOfContentType);
  }

  public void isAnErrorWithCodeAndMessage(String errorCode, String errorMessage) {
    // Can use any fixed body template here
    String expectedError = String.format("{\n" +
        "     \"errorCode\" : \"%s\",\n" +
        "     \"errorMessage\" : \"%s\"" +
        "\n}", errorCode, errorMessage);
    assertThat(response().body()).isEqualTo(expectedError);
  }

  private HttpResponse<String> response() {
    return responseSupplier.get();
  }
}
