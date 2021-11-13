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

ENV REPOSITORY /home/ubuntu/server
ENV JAR_NAME $(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
ENV JAR_PATH $REPOSITORY/build/libs/$JAR_NAME
ENV JVM_OPTS -Dspring.profiles.active=live -Dspring.config.location=$REPOSITORY/application-oauth.yml, $REPOSITORY/application.yml

COPY --from=builder build/libs/brtrip-0.0.1-SNAPSHOT.jar brtrip.jar
VOLUME ["/var/log"]

EXPOSE 8080
ENTRYPOINT ["nohup", "java", "$JVM_OPTS", "-jar", "$JAR_PATH", ">>", "/home/ubuntu/deploy.log 2>&1 &"]