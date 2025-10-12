FROM eclipse-temurin:21-jdk-jammy
LABEL authors="dylan"

ARG JAR_FILE=target/notes-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
