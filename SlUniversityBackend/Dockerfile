# Use Java 17 (you can change to 21 if needed)
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file (after building)
COPY target/*.jar app.jar

# Run the JAR
ENTRYPOINT ["java","-jar","/app/app.jar"]
