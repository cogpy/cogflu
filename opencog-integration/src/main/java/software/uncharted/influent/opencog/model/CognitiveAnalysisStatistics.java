package software.uncharted.influent.opencog.model;

import java.util.Objects;

/**
 * Statistics about cognitive analysis
 */
public class CognitiveAnalysisStatistics {
    
    private final int entityCount;
    private final int relationshipMapCount;
    private final int totalRelationships;
    private final long lastAnalysisTime;
    
    public CognitiveAnalysisStatistics(int entityCount, int relationshipMapCount, 
                                     int totalRelationships, long lastAnalysisTime) {
        this.entityCount = entityCount;
        this.relationshipMapCount = relationshipMapCount;
        this.totalRelationships = totalRelationships;
        this.lastAnalysisTime = lastAnalysisTime;
    }
    
    public int getEntityCount() { return entityCount; }
    public int getRelationshipMapCount() { return relationshipMapCount; }
    public int getTotalRelationships() { return totalRelationships; }
    public long getLastAnalysisTime() { return lastAnalysisTime; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CognitiveAnalysisStatistics that = (CognitiveAnalysisStatistics) o;
        return entityCount == that.entityCount &&
               relationshipMapCount == that.relationshipMapCount &&
               totalRelationships == that.totalRelationships &&
               lastAnalysisTime == that.lastAnalysisTime;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(entityCount, relationshipMapCount, totalRelationships, lastAnalysisTime);
    }
}