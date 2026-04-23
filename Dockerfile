LABEL authors="daan"

# Build stage
FROM gradle:8.10-jdk23 AS build
WORKDIR /app
COPY . .
ARG APP
RUN ./gradlew :apps:$APP:bootJar --no-daemon

# Runtime stage
FROM openjdk:23-jre-slim

ARG APP
ARG VERSION

COPY --from=build /app/src/apps/$APP/build/libs/$APP-$VERSION.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]