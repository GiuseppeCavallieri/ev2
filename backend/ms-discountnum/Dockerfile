FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ms-discountnum.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/ms-discountnum.jar"]