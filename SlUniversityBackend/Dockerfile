# Stage 1: Build (Optimized for Cache)
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# 1. Copy only maven files first
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw

COPY pom.xml .

# 2. Download dependencies (Cached layer)
RUN ./mvnw dependency:go-offline

# 3. Copy source and build
COPY src ./src
RUN ./mvnw -DskipTests package

# Stage 2: Run (Optimized for Size)
# Use JRE instead of JDK
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]