FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ms-discountfreq.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/ms-discountfreq.jar"]