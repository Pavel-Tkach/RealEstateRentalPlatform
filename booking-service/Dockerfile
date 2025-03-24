FROM openjdk:17.0.2

WORKDIR /app

COPY ./build/libs/booking-service-*.jar backend.jar

ENTRYPOINT ["java", "-jar", "backend.jar"]
