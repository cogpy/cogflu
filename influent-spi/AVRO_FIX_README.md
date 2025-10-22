# Avro Code Generation Fix for FL_SearchResult.java

## Problem

The Avro 1.12.1 code generator creates a buggy `hashCode()` method in `FL_SearchResult.java`. 

### Root Cause

The generated class has:
- An instance field: `private java.lang.Object result;` (line 79)
- A hashCode() method that uses a local variable: `int result = 1;` (line 368)

When the generator tries to create the hash code for the `result` field, it incorrectly references the local variable `result` (an int) instead of the instance field `this.result` (an Object):

```java
result = 31 * result + (result == null ? 0 : result.hashCode());  // WRONG
```

This causes two compilation errors:
1. `bad operand types for binary operator '=='` - cannot compare int to null
2. `int cannot be dereferenced` - cannot call hashCode() on a primitive int

## Solution

The fix is automated using the Maven Antrun Plugin in `pom.xml`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-antrun-plugin</artifactId>
    <version>3.1.0</version>
    <executions>
        <execution>
            <id>fix-avro-hashcode</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>run</goal>
            </goals>
            <configuration>
                <target>
                    <replaceregexp file="${project.basedir}/src/main/java/influent/idl/FL_SearchResult.java"
                        match="result = 31 \* result \+ \(result == null \? 0 : result\.hashCode\(\)\);"
                        replace="result = 31 * result + (this.result == null ? 0 : this.result.hashCode());"
                        byline="true"/>
                </target>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### How It Works

1. Avro generates the Java code in the `generate-sources` phase
2. The antrun plugin runs immediately after in the same `generate-sources` phase
3. It uses regex to replace `result == null` with `this.result == null` on line 370
4. The compiler then successfully compiles the corrected code

### Corrected Code

```java
public int hashCode() {
    int result = 1;
    result = 31 * result + Double.hashCode(matchScore);
    result = 31 * result + (this.result == null ? 0 : this.result.hashCode());  // CORRECT
    return result;
}
```

## No Manual Intervention Required

The fix is completely automatic. When you run:
- `mvn clean compile`
- `mvn clean install`
- `mvn generate-sources`

The fix is applied automatically during the build process. No manual steps are needed.

## Alternative Solutions Considered

1. **Custom Avro Template**: Would require maintaining custom Avro generator templates
2. **Manual sed script**: Would require developers to remember to run it after clean builds
3. **Rename the field**: Would break the Avro schema and API compatibility

The Maven antrun plugin solution is the most maintainable and least intrusive approach.
