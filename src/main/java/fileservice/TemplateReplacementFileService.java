package fileservice;

@FunctionalInterface
public interface TemplateReplacementFileService  {

  String replacePlaceHolder(String template, TemplateData data);

  class TemplateData {

    private final String name;
    private final String queryDetails;
    private final String date;

    public TemplateData(String name, String queryDetails, String date) {
      this.name = name;
      this.queryDetails = queryDetails;
      this.date = date;
    }

    public String getName() {
      return name;
    }

    public String getQueryDetails() {
      return queryDetails;
    }

    public String getDate() {
      return date;
    }
  }
}
