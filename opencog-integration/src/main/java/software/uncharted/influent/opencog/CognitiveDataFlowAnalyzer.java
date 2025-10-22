package software.uncharted.influent.opencog;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.uncharted.influent.opencog.model.*;

/**
 * Cognitive Data Flow Analyzer powered by OpenCog
 *
 * <p>This class provides intelligent analysis of transaction flows using OpenCog's cognitive
 * computing capabilities, including pattern recognition, anomaly detection, and behavioral analysis
 * through neural-symbolic reasoning.
 */
@Singleton
public class CognitiveDataFlowAnalyzer {

  private static final Logger logger = LoggerFactory.getLogger(CognitiveDataFlowAnalyzer.class);

  private final AtomSpaceBridge atomSpaceBridge;
  private final Map<String, CognitiveInsight> entityInsights;
  private final Map<String, List<String>> entityRelationships;

  @Inject
  public CognitiveDataFlowAnalyzer(AtomSpaceBridge atomSpaceBridge) {
    this.atomSpaceBridge = atomSpaceBridge;
    this.entityInsights = new ConcurrentHashMap<>();
    this.entityRelationships = new ConcurrentHashMap<>();

    logger.info("Cognitive Data Flow Analyzer initialized with OpenCog backend");
  }

  /** Analyze transaction flow data and build cognitive representation */
  public CompletableFuture<AnalysisResult> analyzeTransactionFlow(TransactionFlowData flowData) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            logger.info(
                "Starting cognitive analysis of transaction flow with {} entities",
                flowData.getEntities().size());

            // Phase 1: Entity ingestion into AtomSpace
            ingestEntities(flowData.getEntities());

            // Phase 2: Relationship mapping
            mapRelationships(flowData.getTransactions());

            // Phase 3: Pattern recognition
            List<CognitivePattern> patterns = identifyPatterns();

            // Phase 4: Anomaly detection
            List<AnomalyDetection> anomalies = detectAnomalies(0.8);

            // Phase 5: Behavioral analysis
            Map<String, EntityBehavior> behaviors = analyzeBehaviors(flowData.getEntities());

            // Phase 6: Generate insights
            List<CognitiveInsight> insights = generateInsights();

            AnalysisResult result =
                new AnalysisResult(
                    patterns, anomalies, behaviors, insights, System.currentTimeMillis());

            logger.info(
                "Cognitive analysis completed: {} patterns, {} anomalies, {} insights",
                patterns.size(),
                anomalies.size(),
                insights.size());

            return result;

          } catch (Exception e) {
            logger.error("Cognitive analysis failed", e);
            throw new RuntimeException("Cognitive analysis failed", e);
          }
        });
  }

  /** Ingest entities into the OpenCog AtomSpace */
  private void ingestEntities(List<TransactionEntity> entities) {
    logger.debug("Ingesting {} entities into AtomSpace", entities.size());

    for (TransactionEntity entity : entities) {
      try {
        Map<String, Object> properties = buildEntityProperties(entity);
        String atomId = atomSpaceBridge.addTransactionEntity(entity.getId(), properties);

        // Store for later reference
        entityInsights.put(entity.getId(), new CognitiveInsight(entity.getId(), atomId));

      } catch (Exception e) {
        logger.warn("Failed to ingest entity {}: {}", entity.getId(), e.getMessage());
      }
    }
  }

  /** Build properties map for an entity */
  private Map<String, Object> buildEntityProperties(TransactionEntity entity) {
    Map<String, Object> properties = new HashMap<>();

    properties.put("type", entity.getType());
    properties.put("account_type", entity.getAccountType());
    properties.put("creation_date", entity.getCreationDate());
    properties.put("status", entity.getStatus());

    if (entity.getAmount() != null) {
      properties.put("amount", entity.getAmount());
    }

    if (entity.getLocation() != null) {
      properties.put("location", entity.getLocation());
    }

    // Add statistical properties
    properties.put("transaction_count", entity.getTransactionCount());
    properties.put("total_volume", entity.getTotalVolume());
    properties.put("average_amount", entity.getAverageAmount());

    return properties;
  }

  /** Map relationships between entities based on transactions */
  private void mapRelationships(List<Transaction> transactions) {
    logger.debug("Mapping relationships from {} transactions", transactions.size());

    for (Transaction transaction : transactions) {
      try {
        String fromEntity = transaction.getFromEntity();
        String toEntity = transaction.getToEntity();
        double amount = transaction.getAmount();

        // Calculate relationship strength based on amount and frequency
        double strength = calculateRelationshipStrength(fromEntity, toEntity, amount);

        // Create relationship in AtomSpace
        atomSpaceBridge.createRelationship("transacts_with", fromEntity, toEntity, strength);

        // Track relationships
        entityRelationships.computeIfAbsent(fromEntity, k -> new ArrayList<>()).add(toEntity);

        // Create temporal relationship if timing is significant
        if (isTemporallySignificant(transaction)) {
          atomSpaceBridge.createRelationship("temporal_sequence", fromEntity, toEntity, 0.7);
        }

      } catch (Exception e) {
        logger.warn(
            "Failed to map relationship for transaction {}: {}",
            transaction.getId(),
            e.getMessage());
      }
    }
  }

  /** Calculate relationship strength between entities */
  private double calculateRelationshipStrength(String fromEntity, String toEntity, double amount) {
    // Simple strength calculation - can be enhanced with more sophisticated metrics
    double frequency = getTransactionFrequency(fromEntity, toEntity);
    double amountNormalized = Math.min(1.0, amount / 100000.0); // Normalize to [0,1]

    return Math.min(1.0, (frequency * 0.6) + (amountNormalized * 0.4));
  }

  /** Get transaction frequency between two entities */
  private double getTransactionFrequency(String fromEntity, String toEntity) {
    List<String> relationships = entityRelationships.get(fromEntity);
    if (relationships == null) return 0.1;

    long count = relationships.stream().filter(to -> to.equals(toEntity)).count();

    return Math.min(1.0, count / 10.0); // Normalize to [0,1]
  }

  /** Check if a transaction is temporally significant */
  private boolean isTemporallySignificant(Transaction transaction) {
    // Simple heuristic - can be enhanced with more sophisticated temporal analysis
    return transaction.getAmount() > 50000
        || transaction.getTimestamp().getHour() < 6
        || transaction.getTimestamp().getHour() > 22;
  }

  /** Identify cognitive patterns in the transaction data */
  private List<CognitivePattern> identifyPatterns() {
    logger.debug("Identifying cognitive patterns");

    List<CognitivePattern> patterns = new ArrayList<>();

    try {
      // Pattern 1: Circular transaction patterns
      List<Map<String, Object>> circularPatterns =
          atomSpaceBridge.executePatternMatching("circular_transaction_pattern");

      for (Map<String, Object> patternData : circularPatterns) {
        patterns.add(
            new CognitivePattern(
                "circular_flow",
                "Circular transaction pattern detected",
                (Double) patternData.get("confidence"),
                (List<String>) patternData.get("matches")));
      }

      // Pattern 2: Hub entities (high connectivity)
      List<Map<String, Object>> hubPatterns =
          atomSpaceBridge.executePatternMatching("high_connectivity_hub");

      for (Map<String, Object> patternData : hubPatterns) {
        patterns.add(
            new CognitivePattern(
                "transaction_hub",
                "High-connectivity transaction hub identified",
                (Double) patternData.get("confidence"),
                (List<String>) patternData.get("matches")));
      }

      // Pattern 3: Temporal clustering
      List<Map<String, Object>> temporalPatterns =
          atomSpaceBridge.executePatternMatching("temporal_clustering");

      for (Map<String, Object> patternData : temporalPatterns) {
        patterns.add(
            new CognitivePattern(
                "temporal_cluster",
                "Temporal transaction clustering detected",
                (Double) patternData.get("confidence"),
                (List<String>) patternData.get("matches")));
      }

    } catch (Exception e) {
      logger.error("Pattern identification failed", e);
    }

    return patterns;
  }

  /** Detect anomalies using OpenCog's attention mechanisms */
  private List<AnomalyDetection> detectAnomalies(double threshold) {
    logger.debug("Detecting anomalies with threshold {}", threshold);

    List<AnomalyDetection> anomalies = new ArrayList<>();

    try {
      List<Map<String, Object>> anomalyData = atomSpaceBridge.detectAnomalies(threshold);

      for (Map<String, Object> anomaly : anomalyData) {
        String entityId = (String) anomaly.get("entity_id");
        Double score = (Double) anomaly.get("anomaly_score");
        String type = (String) anomaly.get("anomaly_type");
        String description = (String) anomaly.get("description");

        anomalies.add(
            new AnomalyDetection(entityId, score, type, description, System.currentTimeMillis()));
      }

    } catch (Exception e) {
      logger.error("Anomaly detection failed", e);
    }

    return anomalies;
  }

  /** Analyze entity behaviors using cognitive insights */
  private Map<String, EntityBehavior> analyzeBehaviors(List<TransactionEntity> entities) {
    logger.debug("Analyzing behaviors for {} entities", entities.size());

    Map<String, EntityBehavior> behaviors = new HashMap<>();

    for (TransactionEntity entity : entities) {
      try {
        Map<String, Object> insights = atomSpaceBridge.getCognitiveInsights(entity.getId());

        EntityBehavior behavior =
            new EntityBehavior(
                entity.getId(),
                (Double) insights.get("cognitive_score"),
                (Double) insights.get("attention_level"),
                (Integer) insights.get("relationship_count"),
                (List<String>) insights.get("behavioral_patterns"));

        behaviors.put(entity.getId(), behavior);

      } catch (Exception e) {
        logger.warn("Failed to analyze behavior for entity {}: {}", entity.getId(), e.getMessage());
      }
    }

    return behaviors;
  }

  /** Generate comprehensive cognitive insights */
  private List<CognitiveInsight> generateInsights() {
    logger.debug("Generating cognitive insights");

    List<CognitiveInsight> insights = new ArrayList<>();

    try {
      // Generate insights using PLN inference
      List<Map<String, Object>> inferenceResults =
          atomSpaceBridge.runPLNInference("transaction_flow_analysis", 10);

      for (Map<String, Object> result : inferenceResults) {
        String query = (String) result.get("query");
        String inferenceResult = (String) result.get("inference_result");
        Double confidence = (Double) result.get("confidence");

        insights.add(
            new CognitiveInsight(
                "inference_" + UUID.randomUUID().toString(),
                inferenceResult,
                confidence,
                "PLN inference result for " + query));
      }

    } catch (Exception e) {
      logger.error("Insight generation failed", e);
    }

    return insights;
  }

  /** Reset the cognitive analysis state */
  public void reset() {
    try {
      atomSpaceBridge.clearAtomSpace();
      entityInsights.clear();
      entityRelationships.clear();
      logger.info("Cognitive analyzer state reset");

    } catch (Exception e) {
      logger.error("Failed to reset cognitive analyzer", e);
    }
  }

  /** Get current statistics about the cognitive analysis */
  public CognitiveAnalysisStatistics getStatistics() {
    return new CognitiveAnalysisStatistics(
        entityInsights.size(),
        entityRelationships.size(),
        entityRelationships.values().stream().mapToInt(List::size).sum(),
        System.currentTimeMillis());
  }
}
