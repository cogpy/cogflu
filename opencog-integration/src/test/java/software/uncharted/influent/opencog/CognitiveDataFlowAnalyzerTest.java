package software.uncharted.influent.opencog;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import software.uncharted.influent.opencog.model.*;

/** Test class for OpenCog integration with Influent */
public class CognitiveDataFlowAnalyzerTest {

  private CognitiveDataFlowAnalyzer analyzer;
  private AtomSpaceBridge atomSpaceBridge;

  @Before
  public void setUp() {
    atomSpaceBridge = new AtomSpaceBridge();
    analyzer = new CognitiveDataFlowAnalyzer(atomSpaceBridge);
  }

  @After
  public void tearDown() {
    if (analyzer != null) {
      analyzer.reset();
    }
    if (atomSpaceBridge != null) {
      atomSpaceBridge.shutdown();
    }
  }

  @Test
  public void testBasicOpenCogIntegration() {
    // Test concept node creation as proxy for Python bridge functionality
    String conceptId = atomSpaceBridge.createConceptNode("TestConcept");
    assertNotNull("Concept ID should not be null", conceptId);
    assertTrue("Concept ID should not be empty", !conceptId.isEmpty());
  }

  @Test
  public void testConceptNodeCreation() {
    // Test concept node creation
    String conceptId = atomSpaceBridge.createConceptNode("TestConcept");
    assertNotNull("Concept ID should not be null", conceptId);
    assertTrue("Concept ID should not be empty", !conceptId.isEmpty());
  }
}
