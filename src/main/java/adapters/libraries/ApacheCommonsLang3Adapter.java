package adapters.libraries;

import core.usecases.ports.outgoing.StringReplacer;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

// Inverting the dependencies on libraries can be done, but so it does not pollute the core package
// It is a decision to be made, but as this library is very common, it is normally ok to allow it to be
// used in the core package
public class ApacheCommonsLang3Adapter implements StringReplacer {
  @Override
  public Function<String, String> replaceValue(String placeholderString, String replacement) {
    return valueWithPlaceholders -> StringUtils.replace(valueWithPlaceholders, placeholderString, replacement);
  }
}
