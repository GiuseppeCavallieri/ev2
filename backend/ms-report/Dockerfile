FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ms-report.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/ms-report.jar"]