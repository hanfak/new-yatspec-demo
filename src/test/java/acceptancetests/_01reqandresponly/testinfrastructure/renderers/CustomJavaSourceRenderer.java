package acceptancetests._01reqandresponly.testinfrastructure.renderers;

import com.googlecode.yatspec.parsing.JavaSource;
import com.googlecode.yatspec.parsing.Text;
import com.googlecode.yatspec.rendering.Renderer;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomJavaSourceRenderer implements Renderer<JavaSource> {
  private static final Pattern DOT_CLASS = Pattern.compile("\\.class(\\W|$)");

  public String render(JavaSource javaSource) {
    List<String> lines = lines(removeDotClass(javaSource.value().trim()));
    return (String) lines.stream().map(Text::wordify).collect(Collectors.joining("\n"));
  }

  public static List<String> lines(String sourceCode) {
    return Arrays.asList((String[]) sourceCode.split(System.lineSeparator()).clone());
  }

  public static String removeDotClass(String s) {
    return DOT_CLASS.matcher(s).replaceAll("$1");
  }
}
