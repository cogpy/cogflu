package software.uncharted.influent.opencog.examples;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import software.uncharted.influent.opencog.AtomSpaceBridge;
import software.uncharted.influent.opencog.CognitiveDataFlowAnalyzer;
import software.uncharted.influent.opencog.model.*;

/**
 * Demonstration example showing OpenCog integration with Influent for cognitive transaction flow
 * analysis.
 *
 * <p>This example illustrates how OpenCog's AtomSpace and reasoning capabilities can be used to
 * analyze financial transaction patterns, detect anomalies, and generate cognitive insights.
 */
public class CognitiveTransactionAnalysisDemo {

  public static void main(String[] args) {
    System.out.println(
        "=== OpenCog Integration with Influent: Cognitive Transaction Analysis Demo ===\n");

    try {
      // Initialize the cognitive analysis system
      System.out.println("1. Initializing OpenCog AtomSpace and Cognitive Analyzer...");
      AtomSpaceBridge atomSpaceBridge = new AtomSpaceBridge();
      CognitiveDataFlowAnalyzer analyzer = new CognitiveDataFlowAnalyzer(atomSpaceBridge);

      // Create sample transaction entities
      System.out.println("2. Creating sample transaction entities...");
      TransactionEntity bank1 =
          new TransactionEntity(
              "BANK_001",
              "financial_institution",
              "commercial_bank",
              LocalDateTime.now().minusYears(10),
              "active",
              null,
              "New York",
              1000,
              50000000.0,
              50000.0);

      TransactionEntity bank2 =
          new TransactionEntity(
              "BANK_002",
              "financial_institution",
              "investment_bank",
              LocalDateTime.now().minusYears(8),
              "active",
              null,
              "London",
              800,
              75000000.0,
              93750.0);

      TransactionEntity account1 =
          new TransactionEntity(
              "ACC_12345",
              "customer_account",
              "checking",
              LocalDateTime.now().minusYears(2),
              "active",
              25000.0,
              "California",
              150,
              500000.0,
              3333.33);

      TransactionEntity account2 =
          new TransactionEntity(
              "ACC_67890",
              "customer_account",
              "savings",
              LocalDateTime.now().minusYears(1),
              "active",
              75000.0,
              "Texas",
              75,
              200000.0,
              2666.67);

      TransactionEntity suspicious =
          new TransactionEntity(
              "ACC_99999",
              "customer_account",
              "offshore",
              LocalDateTime.now().minusMonths(1),
              "flagged",
              1000000.0,
              "Cayman Islands",
              10,
              5000000.0,
              500000.0);

      List<TransactionEntity> entities =
          Arrays.asList(bank1, bank2, account1, account2, suspicious);

      // Create sample transactions demonstrating various patterns
      System.out.println("3. Creating sample transactions with patterns...");
      List<Transaction> transactions =
          Arrays.asList(
              // Normal transactions
              new Transaction(
                  "TXN_001",
                  "ACC_12345",
                  "ACC_67890",
                  1000.0,
                  LocalDateTime.now().minusDays(5),
                  "Transfer",
                  "personal"),
              new Transaction(
                  "TXN_002",
                  "ACC_67890",
                  "BANK_001",
                  500.0,
                  LocalDateTime.now().minusDays(4),
                  "Payment",
                  "fee"),

              // Suspicious patterns - large amounts to offshore account
              new Transaction(
                  "TXN_003",
                  "ACC_12345",
                  "ACC_99999",
                  100000.0,
                  LocalDateTime.now().minusDays(3),
                  "Transfer",
                  "investment"),
              new Transaction(
                  "TXN_004",
                  "ACC_67890",
                  "ACC_99999",
                  150000.0,
                  LocalDateTime.now().minusDays(2),
                  "Transfer",
                  "investment"),

              // Circular transaction pattern (potential money laundering)
              new Transaction(
                  "TXN_005",
                  "ACC_99999",
                  "BANK_002",
                  200000.0,
                  LocalDateTime.now().minusDays(1),
                  "Wire Transfer",
                  "business"),
              new Transaction(
                  "TXN_006",
                  "BANK_002",
                  "ACC_12345",
                  180000.0,
                  LocalDateTime.now(),
                  "Wire Transfer",
                  "loan"),

              // Rapid sequence transactions (structuring pattern)
              new Transaction(
                  "TXN_007",
                  "ACC_12345",
                  "ACC_99999",
                  9500.0,
                  LocalDateTime.now().minusHours(2),
                  "Transfer",
                  "gift"),
              new Transaction(
                  "TXN_008",
                  "ACC_12345",
                  "ACC_99999",
                  9800.0,
                  LocalDateTime.now().minusHours(1),
                  "Transfer",
                  "gift"),
              new Transaction(
                  "TXN_009",
                  "ACC_12345",
                  "ACC_99999",
                  9700.0,
                  LocalDateTime.now().minusMinutes(30),
                  "Transfer",
                  "gift"));

      // Package data for analysis
      TransactionFlowData flowData =
          new TransactionFlowData(entities, transactions, LocalDateTime.now());

      // Execute cognitive analysis
      System.out.println("4. Executing cognitive analysis using OpenCog...");
      CompletableFuture<AnalysisResult> analysisResult = analyzer.analyzeTransactionFlow(flowData);

      // Process results
      AnalysisResult result = analysisResult.get();

      // Display cognitive analysis results
      displayResults(result);

      // Show cognitive insights for specific entities
      System.out.println("\n7. Detailed Cognitive Insights:");
      for (TransactionEntity entity : entities) {
        if (result.getBehaviors().containsKey(entity.getId())) {
          EntityBehavior behavior = result.getBehaviors().get(entity.getId());
          System.out.printf("   Entity: %s\n", entity.getId());
          System.out.printf("     Cognitive Score: %.2f\n", behavior.getCognitiveScore());
          System.out.printf("     Attention Level: %.2f\n", behavior.getAttentionLevel());
          System.out.printf("     Relationships: %d\n", behavior.getRelationshipCount());
          System.out.printf("     Behavioral Patterns: %s\n", behavior.getBehavioralPatterns());
          System.out.println();
        }
      }

      // Display system statistics
      CognitiveAnalysisStatistics stats = analyzer.getStatistics();
      System.out.println("8. System Statistics:");
      System.out.printf("   Entities Processed: %d\n", stats.getEntityCount());
      System.out.printf("   Total Relationships: %d\n", stats.getTotalRelationships());
      System.out.printf(
          "   Analysis Completed: %s\n", new java.util.Date(stats.getLastAnalysisTime()));

      // Cleanup
      analyzer.reset();
      atomSpaceBridge.shutdown();

      System.out.println("\n=== Demo completed successfully! ===");
      System.out.println("\nThis demo shows how OpenCog's cognitive computing capabilities");
      System.out.println("can enhance Influent's transaction flow analysis with:");
      System.out.println("- Hypergraph-based knowledge representation");
      System.out.println("- Neural-symbolic pattern recognition");
      System.out.println("- Probabilistic logic network inference");
      System.out.println("- Adaptive attention allocation for anomaly detection");

    } catch (Exception e) {
      System.err.println("Demo failed: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void displayResults(AnalysisResult result) {
    System.out.println("5. Cognitive Pattern Recognition Results:");
    List<CognitivePattern> patterns = result.getPatterns();
    if (patterns.isEmpty()) {
      System.out.println("   No cognitive patterns detected (using mock data)");
    } else {
      for (CognitivePattern pattern : patterns) {
        System.out.printf(
            "   Pattern: %s (%.2f confidence)\n",
            pattern.getPatternType(), pattern.getConfidence());
        System.out.printf("     Description: %s\n", pattern.getDescription());
        System.out.printf("     Entities: %s\n", pattern.getEntities());
      }
    }

    System.out.println("\n6. Anomaly Detection Results:");
    List<AnomalyDetection> anomalies = result.getAnomalies();
    if (anomalies.isEmpty()) {
      System.out.println("   No anomalies detected (using mock data)");
    } else {
      for (AnomalyDetection anomaly : anomalies) {
        System.out.printf(
            "   Anomaly: %s (Score: %.2f)\n", anomaly.getEntityId(), anomaly.getAnomalyScore());
        System.out.printf("     Type: %s\n", anomaly.getAnomalyType());
        System.out.printf("     Description: %s\n", anomaly.getDescription());
      }
    }

    System.out.printf(
        "\nAnalysis Summary: %d patterns, %d anomalies, %d entities analyzed\n",
        patterns.size(), anomalies.size(), result.getBehaviors().size());
  }
}
