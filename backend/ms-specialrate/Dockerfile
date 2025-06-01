FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ms-specialrate.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/ms-specialrate.jar"]