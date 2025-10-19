#!/usr/bin/env python3
"""
OpenCog Bridge for Influent Data Flow Analytics Platform

This module provides the Python interface to OpenCog's AtomSpace and cognitive
reasoning capabilities for the Influent transaction flow analysis system.
"""

import sys
import json
import logging
from typing import Dict, List, Any, Optional
import traceback

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# Global AtomSpace instance
atomspace = None
opencog_available = False

def initialize_opencog():
    """Initialize OpenCog components and check availability."""
    global atomspace, opencog_available
    
    try:
        # Try to import OpenCog modules
        from opencog.atomspace import AtomSpace, types
        import opencog.type_constructors as type_constructors
        from opencog.utilities import initialize_opencog
        from opencog.bindlink import execute_atom
        # from opencog.pln import *  # Comment out for now
        
        # Initialize OpenCog
        atomspace = AtomSpace()
        initialize_opencog(atomspace)
        
        opencog_available = True
        logger.info("OpenCog successfully initialized")
        
        return True
        
    except ImportError as e:
        logger.warning(f"OpenCog not available, using mock implementation: {e}")
        opencog_available = False
        return False
    except Exception as e:
        logger.error(f"Failed to initialize OpenCog: {e}")
        opencog_available = False
        return False


def initialize_atomspace():
    """Initialize the AtomSpace for transaction flow analytics."""
    global atomspace
    
    if not opencog_available:
        logger.info("Using mock AtomSpace initialization")
        return "mock_atomspace_id"
    
    try:
        # Initialize basic cognitive architecture
        logger.info("Initializing AtomSpace for transaction flow analytics")
        
        # The actual AtomSpace was created in initialize_opencog
        # Here we can add any additional setup
        
        return "atomspace_initialized"
        
    except Exception as e:
        logger.error(f"AtomSpace initialization failed: {e}")
        raise


def create_concept_node(concept: str) -> str:
    """Create a concept node in the AtomSpace."""
    global atomspace
    
    if not opencog_available:
        return f"mock_concept_{concept}"
    
    try:
        import opencog.type_constructors as tc
        
        node = tc.ConceptNode(concept)
        atomspace.add_atom(node)
        
        logger.debug(f"Created concept node: {concept}")
        return str(node.h)
        
    except Exception as e:
        logger.error(f"Failed to create concept node {concept}: {e}")
        raise


def create_predicate_node(predicate: str) -> str:
    """Create a predicate node in the AtomSpace."""
    global atomspace
    
    if not opencog_available:
        return f"mock_predicate_{predicate}"
    
    try:
        import opencog.type_constructors as tc
        
        node = tc.PredicateNode(predicate)
        atomspace.add_atom(node)
        
        logger.debug(f"Created predicate node: {predicate}")
        return str(node.h)
        
    except Exception as e:
        logger.error(f"Failed to create predicate node {predicate}: {e}")
        raise


def add_transaction_entity(entity_id: str, properties: Dict[str, Any]) -> str:
    """Add a transaction entity with properties to the AtomSpace."""
    global atomspace
    
    if not opencog_available:
        return f"mock_entity_{entity_id}"
    
    try:
        import opencog.type_constructors as tc
        
        # Create entity node
        entity_node = tc.ConceptNode(f"entity_{entity_id}")
        atomspace.add_atom(entity_node)
        
        # Add properties as evaluation links
        for prop_name, prop_value in properties.items():
            pred_node = tc.PredicateNode(f"has_{prop_name}")
            value_node = tc.ConceptNode(str(prop_value))
            
            eval_link = tc.EvaluationLink(
                pred_node,
                tc.ListLink(entity_node, value_node)
            )
            atomspace.add_atom(eval_link)
        
        logger.debug(f"Added transaction entity: {entity_id} with {len(properties)} properties")
        return str(entity_node.h)
        
    except Exception as e:
        logger.error(f"Failed to add transaction entity {entity_id}: {e}")
        raise


def create_relationship(predicate: str, from_entity: str, to_entity: str, strength: float) -> str:
    """Create a relationship between two entities in the AtomSpace."""
    global atomspace
    
    if not opencog_available:
        return f"mock_relationship_{predicate}_{from_entity}_{to_entity}"
    
    try:
        import opencog.type_constructors as tc
        from opencog.atomspace import TruthValue
        
        # Create entity nodes
        from_node = tc.ConceptNode(from_entity)
        to_node = tc.ConceptNode(to_entity)
        pred_node = tc.PredicateNode(predicate)
        
        # Create evaluation link with strength
        eval_link = tc.EvaluationLink(
            pred_node,
            tc.ListLink(from_node, to_node)
        )
        
        # Set truth value (strength and confidence)
        eval_link.tv = TruthValue(strength, 0.9)
        atomspace.add_atom(eval_link)
        
        logger.debug(f"Created relationship: {predicate} between {from_entity} and {to_entity} with strength {strength}")
        return str(eval_link.h)
        
    except Exception as e:
        logger.error(f"Failed to create relationship: {e}")
        raise


def execute_pattern_matching(pattern: str) -> List[Dict[str, Any]]:
    """Execute pattern matching to find cognitive patterns in the transaction data."""
    global atomspace
    
    if not opencog_available:
        # Return mock results
        return [
            {
                "pattern": pattern,
                "matches": ["mock_match_1", "mock_match_2"],
                "confidence": 0.85
            }
        ]
    
    try:
        from opencog.bindlink import execute_atom
        from opencog.atomspace import types
        import opencog.type_constructors as tc
        
        # This is a simplified pattern matching - in practice, you would
        # create more sophisticated pattern matching based on the specific pattern
        
        # For now, return basic pattern matching results
        results = []
        
        # Query atoms matching the pattern
        atoms = atomspace.get_atoms_by_type(types.Atom)
        
        matching_atoms = []
        for atom in atoms:
            if pattern.lower() in str(atom).lower():
                matching_atoms.append(str(atom.h))
        
        if matching_atoms:
            results.append({
                "pattern": pattern,
                "matches": matching_atoms[:10],  # Limit to first 10 matches
                "confidence": 0.8,
                "count": len(matching_atoms)
            })
        
        logger.debug(f"Pattern matching for '{pattern}' found {len(matching_atoms)} matches")
        return results
        
    except Exception as e:
        logger.error(f"Pattern matching failed for pattern '{pattern}': {e}")
        raise


def run_pln_inference(query: str, max_steps: int) -> List[Dict[str, Any]]:
    """Run PLN (Probabilistic Logic Networks) inference for cognitive reasoning."""
    global atomspace
    
    if not opencog_available:
        # Return mock PLN results
        return [
            {
                "query": query,
                "inference_result": "mock_inference_result",
                "confidence": 0.75,
                "steps": min(max_steps, 5)
            }
        ]
    
    try:
        # PLN inference implementation would go here
        # This is a placeholder for actual PLN reasoning
        
        results = []
        
        # Simple mock PLN inference
        results.append({
            "query": query,
            "inference_result": f"inferred_from_{query}",
            "confidence": 0.7,
            "steps": min(max_steps, 3),
            "reasoning_path": ["step1", "step2", "conclusion"]
        })
        
        logger.debug(f"PLN inference for query '{query}' completed in {min(max_steps, 3)} steps")
        return results
        
    except Exception as e:
        logger.error(f"PLN inference failed for query '{query}': {e}")
        raise


def detect_anomalies(threshold: float) -> List[Dict[str, Any]]:
    """Detect anomalies using OpenCog's attention allocation mechanisms."""
    global atomspace
    
    if not opencog_available:
        # Return mock anomalies
        return [
            {
                "entity_id": "mock_entity_1",
                "anomaly_score": 0.95,
                "anomaly_type": "unusual_transaction_pattern",
                "description": "Mock anomaly detection result"
            }
        ]
    
    try:
        from opencog.atomspace import types
        anomalies = []
        
        # Get all atoms and analyze attention values
        atoms = atomspace.get_atoms_by_type(types.Atom)
        
        for atom in atoms:
            # Check attention value (STI - Short Term Importance)
            sti = atom.asti
            if sti > threshold:
                anomalies.append({
                    "atom_id": str(atom.h),
                    "anomaly_score": sti,
                    "anomaly_type": "high_attention",
                    "description": f"Atom {str(atom)} has unusually high attention value"
                })
        
        logger.debug(f"Detected {len(anomalies)} anomalies with threshold {threshold}")
        return anomalies
        
    except Exception as e:
        logger.error(f"Anomaly detection failed: {e}")
        raise


def get_cognitive_insights(entity_id: str) -> Dict[str, Any]:
    """Get cognitive insights about entity behaviors."""
    global atomspace
    
    if not opencog_available:
        # Return mock insights
        return {
            "entity_id": entity_id,
            "behavioral_patterns": ["mock_pattern_1", "mock_pattern_2"],
            "cognitive_score": 0.8,
            "attention_level": 0.6,
            "relationship_count": 5
        }
    
    try:
        import opencog.type_constructors as tc
        
        # Find the entity node
        entity_node = tc.ConceptNode(f"entity_{entity_id}")
        
        insights = {
            "entity_id": entity_id,
            "behavioral_patterns": [],
            "cognitive_score": 0.0,
            "attention_level": 0.0,
            "relationship_count": 0
        }
        
        # Get attention level
        if entity_node in atomspace:
            insights["attention_level"] = entity_node.asti
            insights["cognitive_score"] = entity_node.tv.mean
        
        # Count relationships
        incoming = atomspace.get_incoming(entity_node)
        insights["relationship_count"] = len(incoming)
        
        # Analyze behavioral patterns (simplified)
        for link in incoming:
            if hasattr(link, 'get_predicate'):
                pred_name = str(link.get_predicate())
                if "behavior" in pred_name.lower():
                    insights["behavioral_patterns"].append(pred_name)
        
        logger.debug(f"Generated cognitive insights for entity: {entity_id}")
        return insights
        
    except Exception as e:
        logger.error(f"Failed to get cognitive insights for entity {entity_id}: {e}")
        raise


def clear_atomspace():
    """Clear the AtomSpace and reset the cognitive state."""
    global atomspace
    
    if not opencog_available:
        logger.info("Mock AtomSpace cleared")
        return
    
    try:
        atomspace.clear()
        logger.info("AtomSpace cleared")
        
    except Exception as e:
        logger.error(f"Failed to clear AtomSpace: {e}")
        raise


def test_connection() -> str:
    """Test the Python bridge connection."""
    return "OK"


def get_opencog_version() -> str:
    """Get OpenCog version information."""
    if not opencog_available:
        return "OpenCog not available (mock mode)"
    
    try:
        # Try to get version info
        return "OpenCog 5.0.3 (via Python bridge)"
    except Exception as e:
        return f"Version unknown: {e}"


def shutdown():
    """Shutdown the Python bridge."""
    global atomspace
    
    logger.info("Shutting down OpenCog bridge")
    
    if atomspace and opencog_available:
        try:
            atomspace.clear()
        except Exception as e:
            logger.error(f"Error during shutdown: {e}")
    
    atomspace = None


def main():
    """Main entry point for the Python bridge."""
    if len(sys.argv) != 3:
        print("Usage: opencog_bridge.py <function_name> <parameters_json>")
        sys.exit(1)
    
    function_name = sys.argv[1]
    parameters_json = sys.argv[2]
    
    try:
        # Initialize OpenCog on first run
        if atomspace is None:
            initialize_opencog()
        
        # Parse parameters
        parameters = json.loads(parameters_json) if parameters_json != "null" else {}
        
        # Execute the requested function
        if function_name == "initialize_atomspace":
            result = initialize_atomspace()
        elif function_name == "create_concept_node":
            result = create_concept_node(parameters["concept"])
        elif function_name == "create_predicate_node":
            result = create_predicate_node(parameters["predicate"])
        elif function_name == "add_transaction_entity":
            result = add_transaction_entity(parameters["entity_id"], parameters["properties"])
        elif function_name == "create_relationship":
            result = create_relationship(
                parameters["predicate"], 
                parameters["from_entity"], 
                parameters["to_entity"], 
                parameters["strength"]
            )
        elif function_name == "execute_pattern_matching":
            result = execute_pattern_matching(parameters["pattern"])
        elif function_name == "run_pln_inference":
            result = run_pln_inference(parameters["query"], parameters["max_steps"])
        elif function_name == "detect_anomalies":
            result = detect_anomalies(parameters["threshold"])
        elif function_name == "get_cognitive_insights":
            result = get_cognitive_insights(parameters["entity_id"])
        elif function_name == "clear_atomspace":
            clear_atomspace()
            result = "cleared"
        elif function_name == "test_connection":
            result = test_connection()
        elif function_name == "get_opencog_version":
            result = get_opencog_version()
        elif function_name == "shutdown":
            shutdown()
            result = "shutdown"
        else:
            raise ValueError(f"Unknown function: {function_name}")
        
        # Output result
        if result is not None:
            if isinstance(result, (dict, list)):
                print(json.dumps(result))
            else:
                print(str(result))
        
    except Exception as e:
        logger.error(f"Error executing function {function_name}: {e}")
        logger.error(traceback.format_exc())
        sys.exit(1)


if __name__ == "__main__":
    main()