FROM openjdk:11-jdk-slim as builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

FROM openjdk:11-jdk-slim
COPY --from=builder build/libs/brtrip-0.0.1-SNAPSHOT.jar brtrip.jar
VOLUME ["/var/log"]

EXPOSE 8080
ENTRYPOINT ["java","-jar","/brtrip.jar"]
