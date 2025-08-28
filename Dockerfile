# syntax=docker/dockerfile:1

# ===== Build =====
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila sin tests
RUN mvn -version
RUN mvn -q -DskipTests package

# ===== Runtime =====
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
