# OpenCog Integration for Influent: Cognitive Data Flow Analytics Platform

This document describes the implementation of OpenCog as an influent data flow analytics platform, transforming Influent into a cognitive computing system for transaction flow analysis.

## Overview

The OpenCog integration adds cognitive computing capabilities to Influent, enabling:

- **Hypergraph-based knowledge representation** via OpenCog's AtomSpace
- **Neural-symbolic pattern recognition** for transaction flow analysis  
- **Probabilistic logic network (PLN) inference** for cognitive reasoning
- **Adaptive attention allocation** for anomaly detection
- **Emergent behavior analysis** through cognitive clustering

## Architecture

### Core Components

1. **AtomSpaceBridge** - Java interface to OpenCog's AtomSpace hypergraph
2. **PythonOpenCogBridge** - Java-Python communication layer with mock fallback
3. **CognitiveDataFlowAnalyzer** - Main cognitive analysis engine
4. **Model Classes** - Data structures for cognitive analysis results

### Integration Flow

```
Transaction Data → AtomSpace Ingestion → Pattern Recognition → Anomaly Detection → Cognitive Insights
```

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8.8+
- Python 3.7+ (for OpenCog integration)
- Optional: OpenCog Python packages (falls back to mock mode if unavailable)

### Building

```bash
# Build the OpenCog integration module
mvn clean compile -pl opencog-integration

# Run tests
mvn test -pl opencog-integration

# Run the demonstration
mvn exec:java -pl opencog-integration -Dexec.mainClass="software.uncharted.influent.opencog.examples.CognitiveTransactionAnalysisDemo"
```

### Installing OpenCog (Optional)

For full OpenCog functionality, install the Python packages:

```bash
cd opencog-integration/src/main/python
pip install -r requirements.txt
```

Note: The system automatically falls back to mock mode if OpenCog is not available.

## Usage Examples

### Basic Cognitive Analysis

```java
// Initialize the cognitive system
AtomSpaceBridge atomSpaceBridge = new AtomSpaceBridge();
CognitiveDataFlowAnalyzer analyzer = new CognitiveDataFlowAnalyzer(atomSpaceBridge);

// Create transaction data
List<TransactionEntity> entities = Arrays.asList(
    new TransactionEntity("BANK_001", "bank", "commercial", ...)
);
List<Transaction> transactions = Arrays.asList(
    new Transaction("TXN_001", "ACC_1", "ACC_2", 1000.0, ...)
);

TransactionFlowData flowData = new TransactionFlowData(entities, transactions, LocalDateTime.now());

// Execute cognitive analysis
CompletableFuture<AnalysisResult> result = analyzer.analyzeTransactionFlow(flowData);

// Process results
AnalysisResult analysis = result.get();
List<CognitivePattern> patterns = analysis.getPatterns();
List<AnomalyDetection> anomalies = analysis.getAnomalies();
Map<String, EntityBehavior> behaviors = analysis.getBehaviors();
```

### Working with AtomSpace

```java
// Direct AtomSpace operations
AtomSpaceBridge bridge = new AtomSpaceBridge();

// Create concepts
String entityId = bridge.createConceptNode("Transaction");
String predicateId = bridge.createPredicateNode("flows_to");

// Add entities with properties
Map<String, Object> properties = Map.of("amount", 1000.0, "type", "wire_transfer");
String atomId = bridge.addTransactionEntity("TXN_001", properties);

// Create relationships
String relationshipId = bridge.createRelationship("transacts_with", "ACC_1", "ACC_2", 0.8);
```

## Cognitive Analysis Features

### Pattern Recognition

The system identifies several types of cognitive patterns:

- **Circular Transaction Patterns** - Potential money laundering indicators
- **Transaction Hubs** - Entities with high connectivity
- **Temporal Clustering** - Time-based transaction groupings
- **Behavioral Anomalies** - Unusual transaction behaviors

### Anomaly Detection

Using OpenCog's attention allocation mechanisms:

- **Attention-based scoring** for unusual entities
- **Threshold-based detection** for anomaly identification
- **Contextual analysis** of behavioral patterns

### Cognitive Insights

The system generates insights including:

- **Cognitive scores** based on entity behavior
- **Attention levels** indicating entity importance
- **Relationship counts** showing connectivity
- **Behavioral patterns** derived from transaction history

## Configuration

### OpenCog Module Configuration

```java
// Guice dependency injection
Injector injector = Guice.createInjector(new OpenCogModule());
CognitiveDataFlowAnalyzer analyzer = injector.getInstance(CognitiveDataFlowAnalyzer.class);
```

### Python Bridge Configuration

The Python bridge automatically detects available OpenCog installation:

- **With OpenCog**: Full cognitive computing capabilities
- **Without OpenCog**: Mock mode with simulated results
- **Timeout**: Configurable execution timeout (default: 60 seconds)

## Performance Considerations

### Scalability

- **Concurrent analysis** via CompletableFuture
- **Batch processing** for large transaction datasets  
- **Incremental updates** to AtomSpace for streaming data

### Memory Management

- **AtomSpace clearing** between analysis sessions
- **Connection pooling** for Python bridge
- **Garbage collection** of temporary atoms

## Troubleshooting

### Common Issues

1. **Python Bridge Timeout**
   - Increase timeout in PythonOpenCogBridge constructor
   - Check Python script execution permissions

2. **OpenCog Import Errors**
   - Install OpenCog packages or accept mock mode
   - Verify Python environment compatibility

3. **Memory Issues** 
   - Clear AtomSpace regularly: `analyzer.reset()`
   - Monitor heap usage during large analyses

### Debug Mode

Enable detailed logging:

```java
// Add SLF4J implementation to see debug output
// The system logs cognitive analysis steps and Python bridge communication
```

## Contributing

To extend the OpenCog integration:

1. **Add new cognitive patterns** in `CognitiveDataFlowAnalyzer`
2. **Extend Python bridge** functionality in `opencog_bridge.py`
3. **Create new model classes** for additional data types
4. **Write tests** for new cognitive capabilities

## Future Enhancements

- **Real-time streaming** analysis for live transaction data
- **Graph neural networks** integration for enhanced pattern recognition
- **Distributed cognition** across multiple AtomSpace instances
- **Advanced PLN reasoning** for complex financial inference
- **Interactive cognitive visualization** for exploring analysis results

## License

This OpenCog integration follows the same license as the parent Influent project (Apache License 2.0).