package acceptancetests._03thirdpartyhttpcall.testinfrastructure.renderers;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.toTitleCase;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isEmpty;


// TODO: Fix use of consecutive BDD words
// TODO anything in a bracket, with out a period or ( at the end add <span class="literal">blah</span> ???
// TODO: Anything with comments at start of line should be ignored. Possibly no???

public class Text {
  private static final Map<String, String> QUOTED_WORDS = new HashMap<>();

  private static final String SPACE = " ";
  private static final String SPLIT_WITH_SPACE = "$1 $2";
  private static final String SPLIT_WITH_NEW_LINE = "$1$2";
  private static final String REMOVED_STARTING_SPACES_AND_DOT = "$1$2";

  private static final String CODE_SYNTAX = "[()\\[\\]{}:;,]+\\s*";
  private static final String NEW_LINES_AND_SPACES_BETWEEN_WORDS = "^(\\b)\\s+(\\b(?:given|when|then|and)\\b)";
  private static final String WORDS_SEPARATED_BY_DOTS = "([a-zA-Z0-9\\)])\\.([a-zA-Z])";
  private static final String WORDS_SEPARATED_BY_CAMEL_CASE = "([a-z])([A-Z])";
  private static final String WORDS_SEPARATED_BY_CAPITALS = "([A-Z])([A-Z][a-z])";
  private static final String MULTIPLE_SPACES_AND_DOTS_ON_NEW_LINE = "^(\\s*)\\.([$_a-zA-Z])";
  private static final String WORDS_WITH_CLOSING_BRACKET_AND_DOTS_IN_BETWEEN = "(\\))\\.([_a-zA-Z])";
  private static final String START_OF_LINE_WITH_SPACES_REGEX = "^(\\s+)([a-zA-Z])";

  private static final Pattern MATCH_START_OF_LINE_WITH_SPACES_REGEX = Pattern.compile(START_OF_LINE_WITH_SPACES_REGEX);
  private static final Pattern MATCH_ONLY_BDD_TERM_AT_START_OF_LINE = Pattern.compile("^\\s+\\b(given|when|then|and)\\b");
  private static final Pattern ALL_WORDS_STARTING_WITH_CAPITAL_APART_FROM_CONSTANTS_REGEX = Pattern.compile("([A-Z][a-z]+)|(A\\s+)");
  private static final Pattern MATCH_WORD_QUOTE_IN_FORMATTED_STRING_REGEX = Pattern.compile("QUOTE[0-9]+", Pattern.CASE_INSENSITIVE);
  private static final Pattern MATCH_QUOTED_TEXT_IN_FORMATTED_STRING_REGEX = Pattern.compile("\"(.*?)\"");
  private static final Pattern MATCH_JSON_STRING_REGEX = Pattern.compile("(\"[{\\[].*?[}\\]]\")");

  public static String wordify(String value) {

    String wordified = replaceAllTextInQuotesWithSubstitute(value)
        .replaceAll(WORDS_WITH_CLOSING_BRACKET_AND_DOTS_IN_BETWEEN, SPLIT_WITH_SPACE)
        .replaceAll(CODE_SYNTAX, SPACE)
        .replaceAll(NEW_LINES_AND_SPACES_BETWEEN_WORDS, SPLIT_WITH_NEW_LINE)
        .replaceAll(WORDS_SEPARATED_BY_DOTS, SPLIT_WITH_SPACE)
        .replaceAll(WORDS_SEPARATED_BY_CAMEL_CASE, SPLIT_WITH_SPACE)
        .replaceAll(WORDS_SEPARATED_BY_CAPITALS, SPLIT_WITH_SPACE)
        .replaceAll(MULTIPLE_SPACES_AND_DOTS_ON_NEW_LINE, REMOVED_STARTING_SPACES_AND_DOT);

    return replaceOriginalQuotes(
        capitalise(
            extraFormatting(
                trimKeepTabs(
                    AllWordsShouldBeLowerCasedUnlessConstants(wordified)))));
  }

  private static String replaceAllTextInQuotesWithSubstitute(String value) {
    if (value.contains("\"\"")) {
      return value;
    }
    int i = 0;
    String newValue = value;
    final Matcher jsonStringMatcher = MATCH_JSON_STRING_REGEX.matcher(value);
    while (jsonStringMatcher.find()) {
      i = i + 1;
      final String key = "QUOTE" + i;
      QUOTED_WORDS.put(key, jsonStringMatcher.group(1));
      newValue = newValue.replace(jsonStringMatcher.group(1), key);
    }
    final Matcher quotedStringMatcher = MATCH_QUOTED_TEXT_IN_FORMATTED_STRING_REGEX.matcher(newValue);
    while (quotedStringMatcher.find()) {
      i = i + 1;
      final String key = "QUOTE" + i;
      QUOTED_WORDS.put(key, quotedStringMatcher.group(1));
      newValue = newValue.replace(quotedStringMatcher.group(1), key);
    }
    return newValue;
  }

  private static String replaceOriginalQuotes(String wordified) {
    return MATCH_WORD_QUOTE_IN_FORMATTED_STRING_REGEX.matcher(wordified)
        .replaceAll(matcher -> QUOTED_WORDS.get(matcher.group()));
  }

  private static String AllWordsShouldBeLowerCasedUnlessConstants(String wordified) {
    return ALL_WORDS_STARTING_WITH_CAPITAL_APART_FROM_CONSTANTS_REGEX
        .matcher(wordified)
        .replaceAll(matcher -> matcher.group().toLowerCase());
  }

  private static String trimKeepTabs(String wordified) {
    Matcher matcher = MATCH_START_OF_LINE_WITH_SPACES_REGEX.matcher(wordified);
    if (matcher.find()) {
      final int lengthOfSpace = matcher.group(1).length();
      final int lengthOfTab = 2;
      final int numberOfTabs = lengthOfSpace / lengthOfTab;

      Matcher bddMatcher = MATCH_ONLY_BDD_TERM_AT_START_OF_LINE.matcher(wordified);
      if (bddMatcher.find()) {
        return wordified.trim();
      }

      return wordified.replaceAll(START_OF_LINE_WITH_SPACES_REGEX, " ".repeat(numberOfTabs) + "$2").stripTrailing();
    }
    return wordified.trim();
  }

  // TODO: anything in brackets, should be capilised, keep commas,
  // ie generated_migration_job_id()  -> GENERATED_MIGRATION_JOB_ID
  // ie THREE_WAY_CALLING REMINDER_CALL -> THREE_WAY_CALLING, REMINDER_CALL -- should keep the comma
  private static String extraFormatting(String wordified) {
//        String pattern = "(\\w+_\\w+)";
//        String pattern = "([_A-Z0-9]+_[A-Z0-9]+)";
//        Pattern r = Pattern.compile(pattern);
//        Matcher m = r.matcher(wordified);
//        if (m.find()) {
//            String pattern1 = "(^\\s*(?:\\b[a-zA-Z]+\\b\\s*)+(?=[:-]))";
//            Pattern r1 = Pattern.compile(pattern1);
//            Matcher m1 = r1.matcher(wordified);
//            if (m1.find()) {
//                return wordified.replaceAll(pattern1, m1.group(1).toLowerCase());
//            }
////            Character.isTitleCase()
//        }
//        // tODO if all capitals then keep, do not lower case it
//        return wordified.toLowerCase();
    return wordified;
  }

  private static String capitalise(String value) {
    if (isEmpty(value)) return value;

    return stream(value.split(lineSeparator()))
        .map(Text::firstCharacterUppercase)
        .collect(joining(lineSeparator()));
  }

  private static String firstCharacterUppercase(String line) {
    return toTitleCase(line.charAt(0)) + line.substring(1);
  }
}
