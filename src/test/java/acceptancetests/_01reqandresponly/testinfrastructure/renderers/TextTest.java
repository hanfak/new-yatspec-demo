package acceptancetests._01reqandresponly.testinfrastructure.renderers;

import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.Table;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;

class TextTest implements WithAssertions {

  @ParameterizedTest
  @Table({
      @Row({"", ""}),
      //@Row({"thisIsJustAStringWithNumbers123AndNumber5", "This is just a string with numbers123and number5"}), //Needs fix
      @Row({"thisIsJustAString", "This is just a string"}),
      @Row({"thisisjustastring", "Thisisjustastring"})
  })
  void basicFormatting(String input, String expectedOutput) {
    assertThat(Text.wordify(input)).isEqualTo(expectedOutput);
  }

  @ParameterizedTest
  @Table({
      @Row({"givenSomeMethodWithNoArg();", "Given some method with no arg"}),
      @Row({"givenSomeMethod(withOneArg);", "Given some method with one arg"}),
      @Row({"givenSomeMethod(withOneArg, andSecondArg);", "Given some method with one arg and second arg"}),
      @Row({"givenSomeMethod(withOneArg, andSecondArgAsMethod());", "Given some method with one arg and second arg as method"}),
      @Row({"givenSomeMethod(withOneArg, andSecondArgAsMethod(thatHasArg));", "Given some method with one arg and second arg as method that has arg"})
  })
  void javaCodeFormatting(String input, String expectedOutput) {
    assertThat(Text.wordify(input)).isEqualTo(expectedOutput);
  }

  @ParameterizedTest
  @Table({
      @Row({"()[]{}:;,", ""}),
      @Row({"()[]{}:;,   ", ""}),
      @Row({"()[]{}:;,hello", "Hello"}),
      @Row({"()[]{}:;,hello()[]{}:;,People", "Hello people"})
  })
  void codeSyntaxShouldBeRemovedAndReplacedWithSpace(String input, String expectedOutput) {
    assertThat(Text.wordify(input)).isEqualTo(expectedOutput);
  }

  @ParameterizedTest
  @Table({
      @Row({"givenSomeMethod().withSomeField(aValue).andAnotherField();", "Given some method with some field a value and another field"}),
      @Row({"givenSomeMethod().withSomeField(valueObjectBuilder().withAValue()).andAnotherField();", "Given some method with some field value object builder with a value and another field"}),
      @Row({".andSomeMethod().withSomeField();", "And some method with some field"}),
      @Row({"   .thenSomeMethod().withSomeField();", "Then some method with some field"})
//            @Row({"whenSomeMethod().withSomeField().", "When some method with some field"}) // TODO: Fix . at end of linem should be removed
  })
  void wordsSeparatedByDots(String input, String expectedOutput) {
    assertThat(Text.wordify(input)).isEqualTo(expectedOutput);

  }

  @ParameterizedTest
  @Table({
      @Row({".withSomeField(aValue).andAnotherField();", "With some field a value and another field"}),
      @Row({"  .withSomeField(aValue).andAnotherField();", " with some field a value and another field"}),
      @Row({"    .withSomeField(aValue).andAnotherField();", "  with some field a value and another field"}),
      @Row({"      .withSomeField(aValue).andAnotherField();", "   with some field a value and another field"}),
      @Row({"       .withSomeField(aValue).andAnotherField();", "   with some field a value and another field"})
  })
  void newLineAndTabsForNonBddWords(String input, String expectedOutput) {
    assertThat(Text.wordify(input)).isEqualTo(expectedOutput);
  }

  @ParameterizedTest
  @Table({
      @Row({"thenSomeMethod().withSomeField();", "Then some method with some field"}),
      @Row({"    whenSomeMethod().withSomeField();", "When some method with some field"}),
      @Row({"andSomeMethod().withSomeField();", "And some method with some field"}),
      @Row({"givenSomeMethod().withSomeField();", "Given some method with some field"})
  })
  void bddWordsAreCorrectlyFormatted(String input, String expectedOutput) {
    assertThat(Text.wordify(input)).isEqualTo(expectedOutput);
  }

  @ParameterizedTest
  @Table({
      @Row({"givenSomeMethodWith(SOME_CONSTANT);", "Given some method with SOME_CONSTANT"}),
      @Row({"givenSomeMethodWith(SOME_CONSTANT, AND_ANOTHER_CONSTANT);", "Given some method with SOME_CONSTANT AND_ANOTHER_CONSTANT"}),
      @Row({"givenSomeMethodWith(SOME_CONSTANT and(ANOTHER_CONSTANT));", "Given some method with SOME_CONSTANT and ANOTHER_CONSTANT"}),
      @Row({"givenSomeMethodWith(SOME_CONSTANT).and(ANOTHER_CONSTANT));", "Given some method with SOME_CONSTANT and ANOTHER_CONSTANT"})
  })
  void constantsRemainCapitalised(String input, String expectedOutput) {
    assertThat(Text.wordify(input)).isEqualTo(expectedOutput);

  }

  @ParameterizedTest
  @Table({
      @Row({"givenACodeHas(\"\");", "Given a code has \"\""}),
      @Row({"givenACodeHas(\"SOME_CONSTANTS_IN_QUOTES\");", "Given a code has \"SOME_CONSTANTS_IN_QUOTES\""}),
      @Row({"givenACodeHas(\"camelCaseInQuotes\");", "Given a code has \"camelCaseInQuotes\""}),
      @Row({"givenACodeHas(\"Some sentence 32\");", "Given a code has \"Some sentence 32\""}),
      @Row({"givenACodeHas(\"camelCaseInQuotes\").and(\"moreCamelCaseInQuotes\");", "Given a code has \"camelCaseInQuotes\" and \"moreCamelCaseInQuotes\""}),
      @Row({"givenACodeHas(\"camelCaseInQuotes\", \"moreCamelCaseInQuotes\");", "Given a code has \"camelCaseInQuotes\" \"moreCamelCaseInQuotes\""}),
      @Row({"givenACodeHas(\"camelCaseInQuotes\", \"moreCamelCaseInQuotes\").and(\"SOME_CONSTANTS_IN_QUOTES\");", "Given a code has \"camelCaseInQuotes\" \"moreCamelCaseInQuotes\" and \"SOME_CONSTANTS_IN_QUOTES\""})
  })
  void stringsInQuotesShouldNotBeChanged(String input, String expectedOutput) {
    assertThat(Text.wordify(input)).isEqualTo(expectedOutput);
  }

  // TODO
  @ParameterizedTest
  @Disabled
  @Table({
      @Row({"forMigrationJobId(generatedMigrationJobId())", " For migration job id GENERATED_MIGRATION_JOB_ID"}),
      @Row({"forMigrationJobId(generatedMigrationJobId)", " For migration job id GENERATED_MIGRATION_JOB_ID"}),
      @Row({"forServiceMigrationId(generatedServiceMigrationId())", " For service migration job id GENERATED_SERVICE_MIGRATION_ID"}),
      @Row({"forServiceMigrationId(generatedServiceMigrationId)", " For service migration job id GENERATED_SERVICE_MIGRATION_ID"}),
      @Row({"forServiceMigrationId(generatedForDirectoryNumber(_01234567891))", " For service migration job id GENERATED_FOR_DIRECTORY_NUMBER_01234567891"})
  })
  void stringThatHaveMethodThatAccessDatabaseTableIdsTurnedToConstants(String input, String expectedOutput) {
    assertThat(Text.wordify(input)).isEqualTo(expectedOutput);
  }

  // TODO
  @ParameterizedTest
  @Disabled
  @Table({
      @Row({"givenACodeHas(CONSTANT_ONE, CONSTANT_TWO);", "Given a code has CONSTANT_ONE and CONSTANT_TWO"}),
      @Row({"givenACodeHas(CONSTANT_ONE, CONSTANT_TWO, CONSTANT_THREE);", "Given a code has CONSTANT_ONE, CONSTANT_TWO and CONSTANT_THREE"}),
      @Row({"givenACodeHas(List.of(CONSTANT_ONE, CONSTANT_TWO, CONSTANT_THREE));", "Given a code has list of CONSTANT_ONE, CONSTANT_TWO and CONSTANT_THREE"})
  })
  void stringsContainingListOfConstantsShouldKeepTheComma(String input, String expectedOutput) {
    assertThat(Text.wordify(input)).isEqualTo(expectedOutput);
  }
}
