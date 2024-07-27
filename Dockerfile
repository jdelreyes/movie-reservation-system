FROM gradle:8-jdk17 AS builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test

FROM openjdk:17

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/movie-reservation-system.jar
COPY --from=builder /home/gradle/src/src/main/resources /app/resources

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/movie-reservation-system.jar"]
