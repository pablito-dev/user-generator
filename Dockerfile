FROM gradle as builder
COPY ./ ./
USER root
RUN gradle build
FROM openjdk:8-jdk-alpine
COPY --from=builder /home/gradle/build/libs/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "./app.jar"]
