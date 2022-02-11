FROM openjdk:11 as build
ARG JAR_FILE=build/libs/
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

