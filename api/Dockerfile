FROM gradle:8.3 AS build

LABEL maintainer="miniyus"

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src
RUN gradle build

FROM openjdk:21-ea-17-slim

ARG JAR_FILE

EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/$JAR_FILE /app/
RUN bash -c 'touch /app/${JAR_FILE}.jar'
ENTRYPOINT ["java", "-jar","/app/${JAR_FILE}.jar"]