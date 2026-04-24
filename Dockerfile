############################
# Build stage
############################
FROM --platform=$BUILDPLATFORM gradle:8.10-jdk23 AS build
WORKDIR /app

COPY . .

ARG APP
ARG VERSION

RUN echo "APP: $APP, VERSION: $VERSION"

RUN --mount=type=cache,target=/home/gradle/.gradle \
    cd src && ./gradlew dependencies --no-daemon || true

RUN --mount=type=cache,target=/home/gradle/.gradle \
    cd src && ./gradlew :apps:$APP:bootJar --no-daemon

############################
# Runtime stage
############################
FROM sapmachine:23-jre-ubuntu-focal AS runner
WORKDIR /app

ARG APP
ARG VERSION

COPY --from=build /app/src/apps/$APP/build/libs/$APP-$VERSION.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["export", "SPRING_PROFILES_ACTIVE=prd", "java","-jar","/app/app.jar"]
