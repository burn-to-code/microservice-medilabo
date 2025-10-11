FROM eclipse-temurin:21-jdk-jammy
LABEL authors="dylan"

ARG JAR_FILE=tarjet/eureka-server-0.0.1-SNAPCHOT.JAR
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]