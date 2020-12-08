package core.usecases.ports.outgoing;

import java.util.function.Function;

public interface StringReplacer {
  Function<String, String> replaceValue(String placeholderString, String replacement);
}
