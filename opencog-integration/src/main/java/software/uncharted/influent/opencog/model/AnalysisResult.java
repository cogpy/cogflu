package software.uncharted.influent.opencog.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Represents the result of cognitive data flow analysis
 */
public class AnalysisResult {
    
    private final List<CognitivePattern> patterns;
    private final List<AnomalyDetection> anomalies;
    private final java.util.Map<String, EntityBehavior> behaviors;
    private final List<CognitiveInsight> insights;
    private final long analysisTimestamp;
    
    public AnalysisResult(List<CognitivePattern> patterns, 
                         List<AnomalyDetection> anomalies,
                         java.util.Map<String, EntityBehavior> behaviors,
                         List<CognitiveInsight> insights,
                         long analysisTimestamp) {
        this.patterns = patterns;
        this.anomalies = anomalies;
        this.behaviors = behaviors;
        this.insights = insights;
        this.analysisTimestamp = analysisTimestamp;
    }
    
    public List<CognitivePattern> getPatterns() { return patterns; }
    public List<AnomalyDetection> getAnomalies() { return anomalies; }
    public java.util.Map<String, EntityBehavior> getBehaviors() { return behaviors; }
    public List<CognitiveInsight> getInsights() { return insights; }
    public long getAnalysisTimestamp() { return analysisTimestamp; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisResult that = (AnalysisResult) o;
        return analysisTimestamp == that.analysisTimestamp &&
               Objects.equals(patterns, that.patterns) &&
               Objects.equals(anomalies, that.anomalies) &&
               Objects.equals(behaviors, that.behaviors) &&
               Objects.equals(insights, that.insights);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(patterns, anomalies, behaviors, insights, analysisTimestamp);
    }
}