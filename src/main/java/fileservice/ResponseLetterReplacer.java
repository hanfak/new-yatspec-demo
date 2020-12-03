package fileservice;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class ResponseLetterReplacer implements TemplateReplacementFileService {

    @Override
    public String replacePlaceHolder(String template, TemplateData data) {
        return replaceValue("$<NAME>", data.getName())
                .andThen(replaceValue("$<QUERY_DETAILS>", data.getQueryDetails()))
                .andThen(replaceValue("$<DATE>", data.getDate()))
                .apply(template);
    }

    private Function<String, String> replaceValue(String placeholderString, String replacement) {
        return valueWithPlaceholders -> StringUtils.replace(valueWithPlaceholders, placeholderString, replacement);
    }
}
