FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ms-user.jar
ENTRYPOINT ["java","-jar","/ms-user.jar"]