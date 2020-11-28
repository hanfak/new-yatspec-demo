package domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Activity {

  private final Integer id;
  private final Boolean completed;
  private final String title;
  private final String dueDate;

  @JsonCreator
  public Activity(
      @JsonProperty("id") Integer id,
      @JsonProperty("completed") Boolean completed,
      @JsonProperty("title") String title,
      @JsonProperty("dueDate") String dueDate) {
    this.id = id;
    this.completed = completed;
    this.title = title;
    this.dueDate = dueDate;
  }

  public Integer getId() {
    return id;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public String getTitle() {
    return title;
  }

  public String getDueDate() {
    return dueDate;
  }
}
