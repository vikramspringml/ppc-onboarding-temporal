FROM adoptopenjdk/openjdk11:latest

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} temporal-springboot-demo.jar

ENV SPRING_PROFILE=TRACE
ENTRYPOINT [ "java", "-Dlogging.level.gov=${SPRING_PROFILE}", "-jar", "/temporal-springboot-demo.jar" ]