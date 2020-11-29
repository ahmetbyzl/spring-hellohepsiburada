FROM openjdk:12-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
EXPOSE 8080
EXPOSE 11130
ENTRYPOINT ["java","-jar","/application.jar"]
