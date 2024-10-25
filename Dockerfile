FROM quay.io/keycloak/keycloak:23.0.7

ARG APP_JAR=./gateway-service/build/libs/gateway-service-*.jar

COPY ${APP_JAR} /opt/keycloak/providers
