package acceptancetests._02databasestubpriming.testinfrastructure.stub;

import java.util.StringJoiner;

public class CharacterInfoRecord {

  private final Integer id;
  private final Integer characterInfoId;
  private final Integer personId;
  private final String birthYear;
  private final String personName;

  public CharacterInfoRecord(Integer id, Integer characterInfoId, Integer personId, String birthYear, String personName) {
    this.id = id;
    this.characterInfoId = characterInfoId;
    this.personId = personId;
    this.birthYear = birthYear;
    this.personName = personName;
  }

  public Integer getId() {
    return id;
  }

  public Integer getCharacterInfoId() {
    return characterInfoId;
  }

  public Integer getPersonId() {
    return personId;
  }

  public String getBirthYear() {
    return birthYear;
  }

  public String getPersonName() {
    return personName;
  }

  public static class CharacterInfoRecordBuilder {
    private Integer id;
    private Integer characterInfoId;
    private Integer personId;
    private String birthYear;
    private String personName;

    private CharacterInfoRecordBuilder() {
    }

    public static CharacterInfoRecordBuilder characterInfoRecord() {
      return new CharacterInfoRecordBuilder();
    }

    public CharacterInfoRecordBuilder withId(Integer id) {
      this.id = id;
      return this;
    }

    public CharacterInfoRecordBuilder withCharacterInfoId(Integer characterInfoId) {
      this.characterInfoId = characterInfoId;
      return this;
    }

    public CharacterInfoRecordBuilder withPersonId(Integer personId) {
      this.personId = personId;
      return this;
    }

    public CharacterInfoRecordBuilder withBirthYear(String birthYear) {
      this.birthYear = birthYear;
      return this;
    }

    public CharacterInfoRecordBuilder withPersonName(String personName) {
      this.personName = personName;
      return this;
    }

    public CharacterInfoRecord build() {
      return new CharacterInfoRecord(id, characterInfoId, personId, birthYear, personName);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CharacterInfoRecord that = (CharacterInfoRecord) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (characterInfoId != null ? !characterInfoId.equals(that.characterInfoId) : that.characterInfoId != null)
      return false;
    if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
    if (birthYear != null ? !birthYear.equals(that.birthYear) : that.birthYear != null) return false;
    return personName != null ? personName.equals(that.personName) : that.personName == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (characterInfoId != null ? characterInfoId.hashCode() : 0);
    result = 31 * result + (personId != null ? personId.hashCode() : 0);
    result = 31 * result + (birthYear != null ? birthYear.hashCode() : 0);
    result = 31 * result + (personName != null ? personName.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", CharacterInfoRecord.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("characterInfoId=" + characterInfoId)
        .add("personId=" + personId)
        .add("birthYear='" + birthYear + "'")
        .add("personName='" + personName + "'")
        .toString();
  }
}
