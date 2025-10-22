package software.uncharted.influent.opencog.model;

import java.time.LocalDateTime;
import java.util.Objects;

/** Represents a transaction between entities */
public class Transaction {

  private final String id;
  private final String fromEntity;
  private final String toEntity;
  private final double amount;
  private final LocalDateTime timestamp;
  private final String description;
  private final String category;

  public Transaction(
      String id,
      String fromEntity,
      String toEntity,
      double amount,
      LocalDateTime timestamp,
      String description,
      String category) {
    this.id = id;
    this.fromEntity = fromEntity;
    this.toEntity = toEntity;
    this.amount = amount;
    this.timestamp = timestamp;
    this.description = description;
    this.category = category;
  }

  public String getId() {
    return id;
  }

  public String getFromEntity() {
    return fromEntity;
  }

  public String getToEntity() {
    return toEntity;
  }

  public double getAmount() {
    return amount;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public String getDescription() {
    return description;
  }

  public String getCategory() {
    return category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Transaction that = (Transaction) o;
    return Double.compare(that.amount, amount) == 0
        && Objects.equals(id, that.id)
        && Objects.equals(fromEntity, that.fromEntity)
        && Objects.equals(toEntity, that.toEntity)
        && Objects.equals(timestamp, that.timestamp)
        && Objects.equals(description, that.description)
        && Objects.equals(category, that.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fromEntity, toEntity, amount, timestamp, description, category);
  }
}
