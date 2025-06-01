FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ms-rack.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/ms-rack.jar"]