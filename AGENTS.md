# AGENTS.md

## Cursor Cloud specific instructions

This repo ("Influent" / "Cogflu") is a Java 17 + Maven multi-module monorepo (visual
analytics for transaction flow). Standard commands live in `README.md`,
`BUILD_STATUS.md`, and `.github/workflows/maven-full-pipeline.yml`; the notes below only
capture non-obvious, environment-specific gotchas.

### Toolchain (already provisioned in the VM snapshot)
- **JDK 17 is mandatory.** The Maven Enforcer plugin hard-locks the JVM to `[17,18)`
  (see root `pom.xml`), so the base image's JDK 21 is rejected. JDK 17 lives at
  `/opt/java/jdk-17`; Maven 3.9.9 at `/opt/maven` (also `mvn` on `PATH`). `JAVA_HOME`
  and `PATH` are exported from `~/.bashrc`. If you launch a non-login shell, set
  `JAVA_HOME=/opt/java/jdk-17` explicitly.

### Build / lint / test (run from repo root)
- Build all 23 modules: `mvn -B -ntp clean install -DskipTests` (~2 min).
- Lint (Spotless / google-java-format): `mvn -B -ntp spotless:check`
  (auto-fix with `mvn spotless:apply`). Spotless also runs as part of `install`.
- Tests: `mvn -B -ntp test`.
- **Flaky tests:** clustering tests in `ensemble-clustering` (notably
  `com.oculusinfo.ml.distance.TestTemporalDistance` and the hierarchical
  cluster-order assertions) can intermittently fail in a full reactor `mvn test`
  because bucket/cluster ordering is non-deterministic. They pass reliably when the
  module is run in isolation (`mvn test -pl ensemble-clustering -Dtest=...`); re-run
  before assuming a real regression. The root-level `DebugClustering.java` is a
  print-only note about this ordering behaviour.

### Running the example web apps — important caveats
The example WAR apps (`kiva`, `bitcoin`, `walker`, `influent-app`) do **not** run
end-to-end out of the box:
- `mvn jetty:run` is broken: the modules pin the legacy
  `org.mortbay.jetty:jetty-maven-plugin:8.1.16` but add a `jetty-server:12.x`
  dependency, causing a Jetty linkage error (`NoClassDefFound`/`Invocable`).
- The built WARs fail to start on a servlet container (e.g. Tomcat 9) because of
  packaged dependency incompatibilities: Guice 7 core is bundled alongside Guava 23
  and the Guice 4.2.3 `guice-servlet`/`guice-multibindings` extensions, which are
  mutually incompatible (`NoSuchMethodError: ImmutableMap$Builder.buildOrThrow`,
  then a Guice `CreationException`).
- External backends are decommissioned: `solr.uncharted.software` no longer resolves
  (entity search is impossible); only the MSSQL demo host
  `influent.uncharted.software:1433` still answers. There is no bundled sample data.
- Net: a working web UI would require fixing the repo's dependency versions AND
  standing up a local Solr + database with imported data (see
  `docs/src/community/developer-docs/how-to/`). Treat that as a project change, not
  routine dev-env setup.

### Exercising core functionality without the web stack
Influent's core "dynamic entity clustering" is the `ensemble-clustering` library and
runs with no external services. Hello-world demo (no external deps):

```bash
mvn -q -pl ensemble-clustering test-compile
mvn -q -pl ensemble-clustering dependency:build-classpath \
  -Dmdep.outputFile=/tmp/ec-cp.txt -DincludeScope=test
java -cp "ensemble-clustering/target/classes:ensemble-clustering/target/test-classes:$(cat /tmp/ec-cp.txt)" \
  com.oculusinfo.ml.unsupervised.EntityClusteringDemo
```

Source:
`ensemble-clustering/src/test/java/com/oculusinfo/ml/unsupervised/EntityClusteringDemo.java`
(same package as the other unsupervised demos).

### Other subprojects
`graphrag/` is a separate embedded Python project (Microsoft GraphRAG) with its own
toolchain; it is not part of the Maven reactor.
