package acceptancetests._02databasestubpriming.testinfrastructure.stub;

import java.util.StringJoiner;

// Caution: As this is a test class, this can be different from prod. Would need to make sure (ie a test)
// that this matches what is used in prod. Even better have this as a prod class
public class SpeciesInfoRecord {

  private final Integer id;
  private final SpeciesInfoId speciesInfoId; // this field will not be used in storing in db, but will be populated in on read from db
  private final Integer personId;
  private final String name;
  private final Float averageHeight;
  private final Integer lifespan;

  public SpeciesInfoRecord(Integer id, SpeciesInfoId speciesInfoId, Integer personId, String name, Float averageHeight, Integer lifespan) {
    this.id = id;
    // Can do non null checks here or set defaults in the fields
    this.speciesInfoId = speciesInfoId;
    this.personId = personId;
    this.name = name;
    this.averageHeight = averageHeight;
    this.lifespan = lifespan;
  }

  public Integer getId() {
    return id;
  }

  public SpeciesInfoId getSpeciesInfoId() {
    return speciesInfoId;
  }

  public Integer getPersonId() {
    return personId;
  }

  public String getName() {
    return name;
  }

  public Float getAverageHeight() {
    return averageHeight;
  }

  public Integer getLifespan() {
    return lifespan;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SpeciesInfoRecord that = (SpeciesInfoRecord) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (speciesInfoId != null ? !speciesInfoId.equals(that.speciesInfoId) : that.speciesInfoId != null) return false;
    if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (averageHeight != null ? !averageHeight.equals(that.averageHeight) : that.averageHeight != null) return false;
    return lifespan != null ? lifespan.equals(that.lifespan) : that.lifespan == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (speciesInfoId != null ? speciesInfoId.hashCode() : 0);
    result = 31 * result + (personId != null ? personId.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (averageHeight != null ? averageHeight.hashCode() : 0);
    result = 31 * result + (lifespan != null ? lifespan.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", SpeciesInfoRecord.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("speciesInfoId=" + speciesInfoId)
        .add("personId=" + personId)
        .add("name='" + name + "'")
        .add("averageHeight=" + averageHeight)
        .add("lifespan=" + lifespan)
        .toString();
  }

  public static class SpeciesInfoRecordBuilder {
    private Integer id;
    private SpeciesInfoId speciesInfoId; // This is auto generated, so dont need to set this, can set this -1
    private Integer personId;
    private String name;
    private Float averageHeight;
    private Integer lifespan;

    private SpeciesInfoRecordBuilder() {
    }

    public static SpeciesInfoRecordBuilder speciesInfoRecord() {
      return new SpeciesInfoRecordBuilder();
    }

    public SpeciesInfoRecordBuilder withId(Integer id) {
      this.id = id;
      return this;
    }

    public SpeciesInfoRecordBuilder withSpeciesInfoId(Integer speciesInfoId) {
      this.speciesInfoId = new SpeciesInfoId(speciesInfoId);
      return this;
    }

    public SpeciesInfoRecordBuilder withPersonId(Integer personId) {
      this.personId = personId;
      return this;
    }

    public SpeciesInfoRecordBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public SpeciesInfoRecordBuilder withAverageHeight(Float averageHeight) {
      this.averageHeight = averageHeight;
      return this;
    }

    public SpeciesInfoRecordBuilder withLifespan(Integer lifespan) {
      this.lifespan = lifespan;
      return this;
    }

    public SpeciesInfoRecord build() {
      return new SpeciesInfoRecord(id, speciesInfoId, personId, name, averageHeight, lifespan);
    }
  }
}
