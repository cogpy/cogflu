package software.uncharted.influent.opencog;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bridge class for executing OpenCog Python scripts from Java.
 *
 * <p>This class manages the communication between the Java-based Influent system and Python-based
 * OpenCog cognitive computing components.
 */
public class PythonOpenCogBridge {

  private static final Logger logger = LoggerFactory.getLogger(PythonOpenCogBridge.class);

  private final ObjectMapper objectMapper;
  private final String pythonScriptPath;
  private final long timeoutSeconds;

  public PythonOpenCogBridge() {
    this.objectMapper = new ObjectMapper();
    this.pythonScriptPath = determinePythonScriptPath();
    this.timeoutSeconds = 60; // Default timeout

    validatePythonEnvironment();
  }

  /** Determine the path to Python scripts */
  private String determinePythonScriptPath() {
    // Try classpath first
    String classpathResource = "python/opencog_bridge.py";
    if (getClass().getClassLoader().getResource(classpathResource) != null) {
      return getClass().getClassLoader().getResource("python/").getPath();
    }

    // Try relative path from target/classes
    String targetClassesPath = "target/classes/python/";
    if (java.nio.file.Files.exists(java.nio.file.Paths.get(targetClassesPath))) {
      return targetClassesPath;
    }

    // Fallback to source directory for development
    return "src/main/python/";
  }

  /** Validate that Python and OpenCog are available */
  private void validatePythonEnvironment() {
    try {
      // Check if Python is available
      CommandLine cmdLine = new CommandLine("python3");
      cmdLine.addArgument("--version");

      DefaultExecutor executor = new DefaultExecutor();
      executor.setExitValue(0);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
      executor.setStreamHandler(streamHandler);

      int exitCode = executor.execute(cmdLine);

      String output = outputStream.toString();
      logger.info("Python environment: {}", output.trim());

      // Check OpenCog availability (will be handled by Python script)

    } catch (Exception e) {
      logger.warn(
          "Python environment validation failed, proceeding with mock mode: {}", e.getMessage());
    }
  }

  /** Execute a Python function without parameters */
  public void execute(String functionName) {
    execute(functionName, Collections.emptyMap());
  }

  /** Execute a Python function with parameters */
  public void execute(String functionName, Map<String, Object> parameters) {
    try {
      executeInternal(functionName, parameters, false);
    } catch (Exception e) {
      logger.error("Failed to execute Python function: " + functionName, e);
      throw new RuntimeException("Python execution failed", e);
    }
  }

  /** Execute a Python function and return the result */
  public String executeWithResult(String functionName, Map<String, Object> parameters) {
    try {
      return executeInternal(functionName, parameters, true);
    } catch (Exception e) {
      logger.error("Failed to execute Python function with result: " + functionName, e);
      throw new RuntimeException("Python execution failed", e);
    }
  }

  /** Internal method to execute Python functions */
  private String executeInternal(
      String functionName, Map<String, Object> parameters, boolean returnResult)
      throws IOException {

    // Create command to execute Python script
    CommandLine cmdLine = new CommandLine("python3");

    // Add the bridge script
    String scriptPath = pythonScriptPath + "opencog_bridge.py";
    cmdLine.addArgument(scriptPath);
    cmdLine.addArgument(functionName);

    // Convert parameters to JSON
    String parametersJson = objectMapper.writeValueAsString(parameters);
    cmdLine.addArgument(parametersJson, false); // Don't handle quoting automatically

    // Set up executor
    DefaultExecutor executor = new DefaultExecutor();
    executor.setExitValue(0);

    // Set timeout
    ExecuteWatchdog watchdog = new ExecuteWatchdog(TimeUnit.SECONDS.toMillis(timeoutSeconds));
    executor.setWatchdog(watchdog);

    // Capture output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
    executor.setStreamHandler(streamHandler);

    try {
      logger.debug("Executing Python function: {} with parameters: {}", functionName, parameters);

      int exitCode = executor.execute(cmdLine);

      String output = outputStream.toString().trim();
      String errorOutput = errorStream.toString().trim();

      if (!errorOutput.isEmpty()) {
        logger.warn("Python script stderr: {}", errorOutput);
      }

      if (returnResult) {
        logger.debug("Python function {} returned: {}", functionName, output);
        return output;
      } else {
        logger.debug("Python function {} executed successfully", functionName);
        return null;
      }

    } catch (ExecuteException e) {
      String errorOutput = errorStream.toString();
      logger.error(
          "Python script execution failed with exit code {}: {}", e.getExitValue(), errorOutput);
      throw new IOException("Python script execution failed: " + errorOutput, e);

    } catch (IOException e) {
      if (watchdog.killedProcess()) {
        logger.error("Python script execution timed out after {} seconds", timeoutSeconds);
        throw new IOException("Python script execution timed out", e);
      } else {
        throw e;
      }
    }
  }

  /** Test the Python bridge connection */
  public boolean testConnection() {
    try {
      String result = executeWithResult("test_connection", Collections.emptyMap());
      return "OK".equals(result);

    } catch (Exception e) {
      logger.error("Python bridge connection test failed", e);
      return false;
    }
  }

  /** Get OpenCog version information */
  public String getOpenCogVersion() {
    try {
      return executeWithResult("get_opencog_version", Collections.emptyMap());

    } catch (Exception e) {
      logger.error("Failed to get OpenCog version", e);
      return "Unknown";
    }
  }

  /** Shutdown the Python bridge */
  public void shutdown() {
    try {
      execute("shutdown");
      logger.info("Python bridge shutdown completed");

    } catch (Exception e) {
      logger.warn("Error during Python bridge shutdown: {}", e.getMessage());
    }
  }
}
