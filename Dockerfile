# Build stage
FROM gradle:8.10-jdk23 AS build
WORKDIR /app

RUN microdnf install findutils

COPY --chown=gradle:gradle . .

ARG APP
RUN ./src/gradlew :apps:$APP:bootJar --no-daemon

# Runtime stage
FROM sapmachine:21.0.5-jre-ubuntu-focal AS runner

LABEL org.opencontainers.image.source = "https://github.com/ijsKoud/appiecal"

ARG APP
ARG VERSION

COPY --from=build /app/src/apps/$APP/build/libs/$APP-$VERSION.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]