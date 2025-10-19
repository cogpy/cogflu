# Spotless Maven Plugin

A minimal Maven plugin for Spotless code formatting.

## Overview

This is a minimal implementation of a Maven plugin that can format Java files in a Maven project. The plugin provides basic code formatting functionality and serves as a foundation for more advanced Spotless integration.

## Features

- Processes all Java files in Maven source directories
- Normalizes line endings (converts to Unix format)
- Removes trailing whitespace
- Limits consecutive empty lines
- Configurable Eclipse formatter file parameter (for future enhancement)

## Usage

### Adding the Plugin to Your Project

Add the plugin to your `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.diffplug.spotless</groupId>
            <artifactId>spotless-maven-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
            <configuration>
                <eclipseFormatFile>${basedir}/eclipse-format.xml</eclipseFormatFile>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### Running the Plugin

Execute the plugin using the following command:

```bash
mvn com.diffplug.spotless:spotless-maven-plugin:spotless
```

### Configuration

- `eclipseFormatFile`: Path to the Eclipse formatter configuration file. Currently used for validation only but can be extended for actual Eclipse formatting.

## Building the Plugin

To build and install the plugin locally:

```bash
cd spotless-maven-plugin
mvn clean install
```

## Current Limitations

This is a minimal implementation with the following limitations:

- Does not actually parse or use Eclipse formatter configuration
- Provides only basic whitespace normalization
- No support for other Spotless formatters (Google Java Format, etc.)

## Future Enhancements

- Integration with actual Eclipse JDT formatter
- Support for additional Spotless formatters
- More configuration options
- Line ending policy configuration
- Integration with Spotless core libraries

## Example

The plugin processes Java files like this:

**Before:**
```java
public class Example {
public void method(){   
    return;   



}
}
```

**After:**
```java
public class Example {
public void method(){
    return;

}
}
```

## Requirements

- Java 11 or higher
- Maven 3.6 or higher