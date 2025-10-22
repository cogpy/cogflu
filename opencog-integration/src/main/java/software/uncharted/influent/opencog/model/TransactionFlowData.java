package software.uncharted.influent.opencog.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/** Represents transaction flow data for cognitive analysis */
public class TransactionFlowData {

  private final List<TransactionEntity> entities;
  private final List<Transaction> transactions;
  private final LocalDateTime analysisWindow;

  public TransactionFlowData(
      List<TransactionEntity> entities,
      List<Transaction> transactions,
      LocalDateTime analysisWindow) {
    this.entities = entities;
    this.transactions = transactions;
    this.analysisWindow = analysisWindow;
  }

  public List<TransactionEntity> getEntities() {
    return entities;
  }

  public List<Transaction> getTransactions() {
    return transactions;
  }

  public LocalDateTime getAnalysisWindow() {
    return analysisWindow;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TransactionFlowData that = (TransactionFlowData) o;
    return Objects.equals(entities, that.entities)
        && Objects.equals(transactions, that.transactions)
        && Objects.equals(analysisWindow, that.analysisWindow);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entities, transactions, analysisWindow);
  }
}
