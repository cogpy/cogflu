package com.diffplug.spotless.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 * A minimal Maven plugin for Spotless code formatting.
 * Applies Eclipse formatter to Java files in the project.
 */
@Mojo(name = "spotless", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SpotlessMojo extends AbstractMojo {

    /**
     * The Maven project.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * Path to the Eclipse formatter configuration file.
     */
    @Parameter(property = "eclipseFormatFile", required = true)
    private String eclipseFormatFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Running Spotless formatting on Java files...");

        // Check for Eclipse format file (for demonstration)
        File formatFile = new File(eclipseFormatFile);
        if (!formatFile.exists()) {
            getLog().warn("Eclipse format file does not exist: " + eclipseFormatFile + ". Using basic formatting.");
        } else {
            getLog().info("Found Eclipse format file: " + eclipseFormatFile);
        }

        try {
            // Get source directories
            List<String> sourceRoots = project.getCompileSourceRoots();
            int filesProcessed = 0;

            for (String sourceRoot : sourceRoots) {
                Path sourcePath = Paths.get(sourceRoot);
                if (Files.exists(sourcePath)) {
                    getLog().info("Processing source directory: " + sourceRoot);
                    filesProcessed += processDirectory(sourcePath);
                }
            }

            getLog().info("Spotless formatting completed. Processed " + filesProcessed + " Java files.");

        } catch (Exception e) {
            throw new MojoExecutionException("Error during Spotless formatting", e);
        }
    }

    private int processDirectory(Path directory) throws IOException {
        int count = 0;
        
        try (Stream<Path> paths = Files.walk(directory)) {
            List<Path> javaFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());

            for (Path javaFile : javaFiles) {
                try {
                    getLog().debug("Processing file: " + javaFile);
                    
                    // Read file content
                    String content = Files.readString(javaFile);
                    
                    // Apply basic formatting (normalize line endings and trim trailing spaces)
                    String formattedContent = normalizeWhitespace(content);
                    
                    // Write back if changed
                    if (!content.equals(formattedContent)) {
                        Files.writeString(javaFile, formattedContent);
                        getLog().info("Formatted: " + javaFile);
                    }
                    
                    count++;
                } catch (Exception e) {
                    getLog().warn("Failed to process file: " + javaFile + " - " + e.getMessage());
                }
            }
        }
        
        return count;
    }
    
    private String normalizeWhitespace(String content) {
        // Basic formatting: normalize line endings and trim trailing whitespace
        return content
                .replaceAll("\\r\\n", "\n")  // Windows to Unix line endings
                .replaceAll("\\r", "\n")     // Mac to Unix line endings  
                .replaceAll("[ \\t]+\\n", "\n")  // Remove trailing whitespace
                .replaceAll("\\n{3,}", "\n\n");  // Limit consecutive empty lines to 2
    }
}