package acceptancetests._02databasepriming.givens;

public class SpeciesInfoRecord {

  private final Integer id;
  private final String name;
  private final Float averageHeight;
  private final Integer lifespan;

  private SpeciesInfoRecord(Integer id, String name, Float averageHeight, Integer lifespan) {
    // Can do non null checks here or set defaults in the fields
    this.id = id;
    this.name = name;
    this.averageHeight = averageHeight;
    this.lifespan = lifespan;
  }

  public Integer getId() {
    return id;
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

  public static class SpeciesInfoRecordBuilder {
    private Integer id;
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
      return new SpeciesInfoRecord(id, name, averageHeight, lifespan);
    }
  }
}
