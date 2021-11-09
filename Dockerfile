FROM openjdk:16-jdk-alpine
ARG JAR_FILE=build/libs/kotlin-0.0.1-SNAPSHOT.jar
ARG ACTIVE_PROFILE=dev
ENV ACTIVE_PROFILE=$ACTIVE_PROFILE
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "/application.jar"]