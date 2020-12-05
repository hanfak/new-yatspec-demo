package usecases.generateresponseletter;

@FunctionalInterface
public interface TemplateReplacementFileService  {

  String replacePlaceHolder(String template, TemplateData templateData);

  class TemplateData {

    private final String name;
    private final String queryDetails;
    private final String date;
    private final long id;

    public TemplateData(String name, String queryDetails, String date, long id) {
      this.name = name;
      this.queryDetails = queryDetails;
      this.date = date;
      this.id = id;
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

    public long getId() {
      return id;
    }
  }
}
