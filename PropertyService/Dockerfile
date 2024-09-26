FROM gradle:8.10-jdk17-alpine AS build

WORKDIR /app

COPY build.gradle.kts gradlew gradlew.bat settings.gradle.kts ./
COPY ./gradle ./gradle
COPY ./src ./src

RUN ./gradlew build --no-daemon

FROM openjdk:17.0.2

WORKDIR /app

COPY --from=build /app/build/libs/property-service-*.jar backend.jar

ENTRYPOINT ["java", "-jar", "backend.jar"]