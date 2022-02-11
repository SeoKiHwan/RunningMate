FROM openjdk:11 as build
ARG JAR_FILE=build/libs/
COPY ${JAR_FILE} *.jar
ENTRYPOINT ["java","-jar","/app.jar"]
