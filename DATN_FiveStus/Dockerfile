FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/DATN_FiveStus.jar-0.0.1-SNAPSHOT.jar DATN_FiveStus.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "DATN_FiveStus.jar"]
