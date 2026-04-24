ARG APP
ARG VERSION

############################
# Build stage
############################
FROM --platform=$BUILDPLATFORM gradle:8.10-jdk23 AS build
WORKDIR /app

COPY . .

RUN --mount=type=cache,target=/home/gradle/.gradle \
    cd src && ./gradlew dependencies --no-daemon || true

RUN --mount=type=cache,target=/home/gradle/.gradle \
    cd src && ./gradlew :apps:$APP:bootJar --no-daemon

############################
# Runtime stage
############################
FROM sapmachine:21.0.5-jre-ubuntu-focal AS runner

WORKDIR /app
COPY --from=build /app/src/apps/$APP/build/libs/$APP-$VERSION.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
