package acceptancetests._02databasestubpriming.testinfrastructure.stub;

public class SpeciesInfoId {
  private final Integer value;

  public SpeciesInfoId(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SpeciesInfoId that = (SpeciesInfoId) o;

    return value != null ? value.equals(that.value) : that.value == null;
  }

  @Override
  public int hashCode() {
    return value != null ? value.hashCode() : 0;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
