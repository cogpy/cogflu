package software.uncharted.influent.opencog.model;

import java.util.List;
import java.util.Objects;

/** Represents a cognitive pattern identified in transaction flow data */
public class CognitivePattern {

  private final String patternType;
  private final String description;
  private final double confidence;
  private final List<String> entities;

  public CognitivePattern(
      String patternType, String description, double confidence, List<String> entities) {
    this.patternType = patternType;
    this.description = description;
    this.confidence = confidence;
    this.entities = entities;
  }

  public String getPatternType() {
    return patternType;
  }

  public String getDescription() {
    return description;
  }

  public double getConfidence() {
    return confidence;
  }

  public List<String> getEntities() {
    return entities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CognitivePattern that = (CognitivePattern) o;
    return Double.compare(that.confidence, confidence) == 0
        && Objects.equals(patternType, that.patternType)
        && Objects.equals(description, that.description)
        && Objects.equals(entities, that.entities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(patternType, description, confidence, entities);
  }
}
