FROM maven:3.8.6-eclipse-temurin-17 AS builder

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/DATN_WebFiveTus-0.0.1-SNAPSHOT.jar DATN_WebFiveTus-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "DATN_WebFiveTus-0.0.1-SNAPSHOT.jar"]