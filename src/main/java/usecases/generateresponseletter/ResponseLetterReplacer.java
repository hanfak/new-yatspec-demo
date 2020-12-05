package usecases.generateresponseletter;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class ResponseLetterReplacer implements TemplateReplacementFileService {

    @Override
    public String replacePlaceHolder(String template, TemplateData templateData) {
        return replaceValue("$<NAME>", templateData.getName())
            .andThen(replaceValue("$<QUERY_DETAILS>", templateData.getQueryDetails()))
            .andThen(replaceValue("$<DATE>", templateData.getDate()))
            .andThen(replaceValue("$<ID>", Long.toString(templateData.getId())))
            .apply(template);
    }

    private Function<String, String> replaceValue(String placeholderString, String replacement) {
        return valueWithPlaceholders -> StringUtils.replace(valueWithPlaceholders, placeholderString, replacement);
    }
}
