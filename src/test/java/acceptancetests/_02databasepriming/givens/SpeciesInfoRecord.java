package acceptancetests._02databasepriming.givens;

import java.util.StringJoiner;

// Caution: As this is a test class, this can be different from prod. Would need to make sure (ie a test)
// that this matches what is used in prod. Even better have this as a prod class
public class SpeciesInfoRecord {

  private final Integer speciesInfoId; // this field will not be used in storing in db, but will be populated in on read from db
  private final Integer personId;
  private final String name;
  private final Float averageHeight;
  private final Integer lifespan;

  SpeciesInfoRecord(Integer speciesInfoId, Integer personId, String name, Float averageHeight, Integer lifespan) {
    // Can do non null checks here or set defaults in the fields
    this.speciesInfoId = speciesInfoId;
    this.personId = personId;
    this.name = name;
    this.averageHeight = averageHeight;
    this.lifespan = lifespan;
  }

  public Integer getSpeciesInfoId() {
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
  public String toString() {
    return new StringJoiner(", ", SpeciesInfoRecord.class.getSimpleName() + "[", "]")
        .add("id=" + personId)
        .add("name='" + name + "'")
        .add("averageHeight=" + averageHeight)
        .add("lifespan=" + lifespan)
        .toString();
  }

  public static class SpeciesInfoRecordBuilder {
    private Integer speciesInfoId = -1; // This is auto generated, so dont need to set this
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
      this.speciesInfoId = speciesInfoId;
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
