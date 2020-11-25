package acceptancetests._02databasepriming.givens;

import java.util.Objects;
import java.util.StringJoiner;

// Caution: As this is a test class, this can be different from prod. Would need to make sure (ie a test)
// that this matches what is used in prod. Even better have this as a prod class
public class SpeciesInfoRecord {

  private final SpeciesInfoId speciesInfoId; // this field will not be used in storing in db, but will be populated in on read from db
  private final Integer personId;
  private final String name;
  private final Float averageHeight;
  private final Integer lifespan;

  public SpeciesInfoRecord(SpeciesInfoId speciesInfoId, Integer personId, String name, Float averageHeight, Integer lifespan) {
    // Can do non null checks here or set defaults in the fields
    this.speciesInfoId = speciesInfoId;
    this.personId = personId;
    this.name = name;
    this.averageHeight = averageHeight;
    this.lifespan = lifespan;
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

    if (speciesInfoId != null ? !speciesInfoId.equals(that.speciesInfoId) : that.speciesInfoId != null) return false;
    if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (averageHeight != null ? !averageHeight.equals(that.averageHeight) : that.averageHeight != null) return false;
    return lifespan != null ? lifespan.equals(that.lifespan) : that.lifespan == null;
  }

  @Override
  public int hashCode() {
    int result = speciesInfoId != null ? speciesInfoId.hashCode() : 0;
    result = 31 * result + (personId != null ? personId.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (averageHeight != null ? averageHeight.hashCode() : 0);
    result = 31 * result + (lifespan != null ? lifespan.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    StringJoiner wrapper = new StringJoiner(", ", SpeciesInfoRecord.class.getSimpleName() + "[", "]");
    StringJoiner alwaysPresentFields = wrapper.merge(new StringJoiner(", ").add("personId=" + personId)
        .add("name='" + name + "'")
        .add("averageHeight=" + averageHeight)
        .add("lifespan=" + lifespan));
    if (Objects.isNull(speciesInfoId)) {
      return alwaysPresentFields
          .toString();
    }
    return alwaysPresentFields
        .add("speciesInfoId=" + speciesInfoId) //To avoid showing speciesInfoId
        .toString();
  }

  public static class SpeciesInfoRecordBuilder {
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
      return new SpeciesInfoRecord(speciesInfoId, personId, name, averageHeight, lifespan);
    }
  }
}
