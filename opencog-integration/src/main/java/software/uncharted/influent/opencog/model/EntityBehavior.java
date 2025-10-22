package software.uncharted.influent.opencog.model;

import java.util.List;
import java.util.Objects;

/** Represents the cognitive behavior analysis of an entity */
public class EntityBehavior {

  private final String entityId;
  private final double cognitiveScore;
  private final double attentionLevel;
  private final int relationshipCount;
  private final List<String> behavioralPatterns;

  public EntityBehavior(
      String entityId,
      double cognitiveScore,
      double attentionLevel,
      int relationshipCount,
      List<String> behavioralPatterns) {
    this.entityId = entityId;
    this.cognitiveScore = cognitiveScore;
    this.attentionLevel = attentionLevel;
    this.relationshipCount = relationshipCount;
    this.behavioralPatterns = behavioralPatterns;
  }

  public String getEntityId() {
    return entityId;
  }

  public double getCognitiveScore() {
    return cognitiveScore;
  }

  public double getAttentionLevel() {
    return attentionLevel;
  }

  public int getRelationshipCount() {
    return relationshipCount;
  }

  public List<String> getBehavioralPatterns() {
    return behavioralPatterns;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EntityBehavior that = (EntityBehavior) o;
    return Double.compare(that.cognitiveScore, cognitiveScore) == 0
        && Double.compare(that.attentionLevel, attentionLevel) == 0
        && relationshipCount == that.relationshipCount
        && Objects.equals(entityId, that.entityId)
        && Objects.equals(behavioralPatterns, that.behavioralPatterns);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        entityId, cognitiveScore, attentionLevel, relationshipCount, behavioralPatterns);
  }
}
