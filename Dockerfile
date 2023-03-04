FROM openjdk:17.0.2-jdk-slim-buster
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} english-backend.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/english-backend.jar"]
