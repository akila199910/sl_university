# Stage 1 — Build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files first (layer caching)
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build JAR (skip tests to speed up; remove -DskipTests for CI)
RUN ./mvnw -DskipTests package

# Stage 2 — Run the application
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Expose backend port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
