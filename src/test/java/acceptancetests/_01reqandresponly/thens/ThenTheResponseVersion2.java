package acceptancetests._01reqandresponly.thens;

import org.assertj.core.api.AbstractAssert;
import org.mockito.internal.util.Supplier;

import java.net.http.HttpResponse;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenTheResponseVersion2 extends AbstractAssert<ThenTheResponseVersion2, HttpResponse<String>> {

  private final Supplier<HttpResponse<String>> response;

  public ThenTheResponseVersion2(Supplier<HttpResponse<String>> httpResponseSupplier) {
    super(httpResponseSupplier.get(), ThenTheResponseVersion2.class);
    this.response = httpResponseSupplier;
  }

  public ThenTheResponseVersion2 hasStatusCode(int expectedStatusCode) {
    assertThat(response.get().statusCode()).isEqualTo(expectedStatusCode);
    return this;
  }

  // Using custom predicates/mathers
  //  See https://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html
  public ThenTheResponseVersion2 hasBody(String expectedBody) {
    // Equivalent to
    // assertThat(response.get().body()).isEqualTo(expectedBody);
    if (Objects.isNull(response.get().body())){
      failWithMessage("Expecting actual not to be null");
    }

    if (!Objects.equals(response.get().body(), expectedBody)) {
      failWithMessage("%nThe expectedBody was %n <%s> %n but actual body was%n <%s>", expectedBody, response.get().body());
    }
    return this;
  }

  public ThenTheResponseVersion2 hasContentType(String expectedContentType) {
    String headValueOfContentType = response.get().headers()
            .firstValue("content-type")
            .orElse("not_found");
    assertThat(headValueOfContentType).isEqualTo(expectedContentType);
    return this;
  }
}
