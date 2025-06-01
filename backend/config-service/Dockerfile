FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} config-service.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/config-service.jar"]