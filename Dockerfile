# Build phase
FROM openjdk:11-jdk-slim as builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

# Run phase
FROM openjdk:11-jdk-slim

COPY --from=builder build/libs/brtrip-0.0.1-SNAPSHOT.jar /app/brtrip.jar
COPY --from=builder src/test/resources/application.yml /app/application.yml
COPY --from=builder src/test/resources/application-oauth.yml /app/application-oauth.yml

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["nohup", "java", "-jar", "brtrip.jar", "-Dspring.profiles.active=live", "-Dspring.config.location=application-oauth.yml, application.yml"]