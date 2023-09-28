FROM gradle:8.3 AS build

LABEL maintainer="miniyus"

COPY --chown=gradle:gradle build.gradle /app/build.gradle
COPY --chown=gradle:gradle src /app/src

WORKDIR /app
RUN gradle build

FROM openjdk:17-jdk-slim AS deployment

ARG JAR_FILE

EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/$JAR_FILE /app.jar

ENTRYPOINT ["java", "-jar","app.jar"]
