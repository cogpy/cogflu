package software.uncharted.influent.opencog;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Guice module for OpenCog integration with Influent
 */
public class OpenCogModule extends AbstractModule {

    @Override
    protected void configure() {
        // Bind the cognitive analyzer as singleton
        bind(CognitiveDataFlowAnalyzer.class);
        
        // Bind the AtomSpace bridge as singleton  
        bind(AtomSpaceBridge.class).in(Singleton.class);
        
        // Bind the Python bridge
        bind(PythonOpenCogBridge.class).in(Singleton.class);
    }
    
    @Provides
    @Singleton
    public AtomSpaceBridge provideAtomSpaceBridge() {
        return new AtomSpaceBridge();
    }
}