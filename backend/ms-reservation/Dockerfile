FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ms-reservation.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/ms-reservation.jar"]