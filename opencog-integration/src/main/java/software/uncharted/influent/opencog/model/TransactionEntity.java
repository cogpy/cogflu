package software.uncharted.influent.opencog.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a transaction entity in the cognitive model
 */
public class TransactionEntity {
    
    private final String id;
    private final String type;
    private final String accountType;
    private final LocalDateTime creationDate;
    private final String status;
    private final Double amount;
    private final String location;
    private final int transactionCount;
    private final double totalVolume;
    private final double averageAmount;
    
    public TransactionEntity(String id, String type, String accountType, LocalDateTime creationDate,
                           String status, Double amount, String location, int transactionCount,
                           double totalVolume, double averageAmount) {
        this.id = id;
        this.type = type;
        this.accountType = accountType;
        this.creationDate = creationDate;
        this.status = status;
        this.amount = amount;
        this.location = location;
        this.transactionCount = transactionCount;
        this.totalVolume = totalVolume;
        this.averageAmount = averageAmount;
    }
    
    public String getId() { return id; }
    public String getType() { return type; }
    public String getAccountType() { return accountType; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public String getStatus() { return status; }
    public Double getAmount() { return amount; }
    public String getLocation() { return location; }
    public int getTransactionCount() { return transactionCount; }
    public double getTotalVolume() { return totalVolume; }
    public double getAverageAmount() { return averageAmount; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return transactionCount == that.transactionCount &&
               Double.compare(that.totalVolume, totalVolume) == 0 &&
               Double.compare(that.averageAmount, averageAmount) == 0 &&
               Objects.equals(id, that.id) &&
               Objects.equals(type, that.type) &&
               Objects.equals(accountType, that.accountType) &&
               Objects.equals(creationDate, that.creationDate) &&
               Objects.equals(status, that.status) &&
               Objects.equals(amount, that.amount) &&
               Objects.equals(location, that.location);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, type, accountType, creationDate, status, amount, location, transactionCount, totalVolume, averageAmount);
    }
}