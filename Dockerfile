FROM tomcat:9-jdk11-openjdk

# Copy WAR files to Tomcat webapps
COPY bitcoin/target/bitcoin-2.0.0.war $CATALINA_HOME/webapps/
COPY influent-app/target/influent-app-2.0.0.war $CATALINA_HOME/webapps/
COPY kiva/target/kiva-2.0.0.war $CATALINA_HOME/webapps/
COPY walker/target/walker-2.0.0.war $CATALINA_HOME/webapps/

# Set JVM options for large datasets
ENV CATALINA_OPTS="-Xmx10240m"

# Expose port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
