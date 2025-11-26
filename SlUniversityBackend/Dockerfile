# ============================
# Stage 1: Build (Optimized)
# ============================
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Fix SSL handshake issues by installing CA certificates
RUN apt-get update && apt-get install -y ca-certificates && update-ca-certificates

# ------------------------
# 1. Copy Maven wrapper
# ------------------------
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# ------------------------
# 2. Copy pom.xml only
#    -> This allows Docker to cache dependencies
# ------------------------
COPY pom.xml .

# Pre-download dependencies with retry protection
RUN ./mvnw dependency:go-offline \
    -Dmaven.wagon.http.retryHandler.count=5 \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=30

# ------------------------
# 3. Copy source and build
# ------------------------
COPY src ./src

RUN ./mvnw -DskipTests \
    -Dmaven.wagon.http.retryHandler.count=5 \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=30 \
    package

# ============================
# Stage 2: Runtime (Light)
# ============================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
