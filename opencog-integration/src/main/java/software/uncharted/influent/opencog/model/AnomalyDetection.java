package software.uncharted.influent.opencog.model;

import java.util.Objects;

/** Represents an anomaly detection result */
public class AnomalyDetection {

  private final String entityId;
  private final double anomalyScore;
  private final String anomalyType;
  private final String description;
  private final long detectionTimestamp;

  public AnomalyDetection(
      String entityId,
      double anomalyScore,
      String anomalyType,
      String description,
      long detectionTimestamp) {
    this.entityId = entityId;
    this.anomalyScore = anomalyScore;
    this.anomalyType = anomalyType;
    this.description = description;
    this.detectionTimestamp = detectionTimestamp;
  }

  public String getEntityId() {
    return entityId;
  }

  public double getAnomalyScore() {
    return anomalyScore;
  }

  public String getAnomalyType() {
    return anomalyType;
  }

  public String getDescription() {
    return description;
  }

  public long getDetectionTimestamp() {
    return detectionTimestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AnomalyDetection that = (AnomalyDetection) o;
    return Double.compare(that.anomalyScore, anomalyScore) == 0
        && detectionTimestamp == that.detectionTimestamp
        && Objects.equals(entityId, that.entityId)
        && Objects.equals(anomalyType, that.anomalyType)
        && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entityId, anomalyScore, anomalyType, description, detectionTimestamp);
  }
}
