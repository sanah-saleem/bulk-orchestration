# === Build stage ===
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom and resolve deps (better caching)
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn -q clean package -DskipTests

# 2) Run stage (lightweight JDK)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy built jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
