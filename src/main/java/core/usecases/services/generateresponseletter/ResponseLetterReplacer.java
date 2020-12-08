package core.usecases.services.generateresponseletter;

import core.usecases.ports.outgoing.StringReplacer;
import core.usecases.ports.outgoing.TemplateReplacementFileService;

public class ResponseLetterReplacer implements TemplateReplacementFileService {

  private final StringReplacer replacer;

  public ResponseLetterReplacer(StringReplacer replacer) {
    this.replacer = replacer;
  }

  @Override
  public String replacePlaceHolder(String template, TemplateData templateData) {
    return replacer.replaceValue("$<NAME>", templateData.getName())
        .andThen(replacer.replaceValue("$<QUERY_DETAILS>", templateData.getQueryDetails()))
        .andThen(replacer.replaceValue("$<DATE>", templateData.getDate()))
        .andThen(replacer.replaceValue("$<ID>", Long.toString(templateData.getId())))
        .apply(template);
  }
}
