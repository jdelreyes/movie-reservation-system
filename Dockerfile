FROM gradle:8-jdk17-alpine AS builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test

FROM openjdk:17-alpine

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/movie-reservation-system.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/movie-reservation-system.jar"]
