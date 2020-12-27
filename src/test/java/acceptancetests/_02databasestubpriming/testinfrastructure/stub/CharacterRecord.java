package acceptancetests._02databasestubpriming.testinfrastructure.stub;

public class CharacterRecord {
  private final Integer id;
  private final Integer personId;
  private final String personName;

  public CharacterRecord(Integer id, Integer personId, String personName) {
    this.id = id;
    this.personId = personId;
    this.personName = personName;
  }

  public Integer getId() {
    return id;
  }

  public Integer getPersonId() {
    return personId;
  }

  public String getPersonName() {
    return personName;
  }
}
