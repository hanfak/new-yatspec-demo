package acceptancetests._02databasestubpriming.thens;

import org.assertj.core.api.AbstractAssert;
import org.mockito.internal.util.Supplier;

import java.net.http.HttpResponse;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenTheResponse extends AbstractAssert<ThenTheResponse, HttpResponse<String>> {

  private final Supplier<HttpResponse<String>> response;

  public ThenTheResponse(Supplier<HttpResponse<String>> httpResponseSupplier) {
    super(httpResponseSupplier.get(), ThenTheResponse.class);
    this.response = httpResponseSupplier;
  }

  public ThenTheResponse hasStatusCode(int expectedStatusCode) {
    assertThat(response.get().statusCode()).isEqualTo(expectedStatusCode);
    return this;
  }

  // Using custom predicates/mathers
  //  See https://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html
  public ThenTheResponse hasBody(String expectedBody) {
    // Equivalent to
    // assertThat(response.get().body()).isEqualTo(expectedBody);
    if (Objects.isNull(response.get().body())) {
      failWithMessage("Expecting actual not to be null");
    }

    if (!Objects.equals(response.get().body(), expectedBody)) {
      failWithMessage("%nThe expectedBody was %n <%s> %n but actual body was%n <%s>", expectedBody, response.get().body());
    }
    return this;
  }

  public ThenTheResponse hasContentType(String expectedContentType) {
    String headValueOfContentType = response.get().headers()
        .firstValue("content-type")
        .orElse("not_found");
    assertThat(headValueOfContentType).isEqualTo(expectedContentType);
    return this;
  }
}
