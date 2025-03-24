FROM openjdk:17.0.2

WORKDIR /app

COPY ./build/libs/property-service-*.jar backend.jar

ENTRYPOINT ["java", "-jar", "backend.jar"]
