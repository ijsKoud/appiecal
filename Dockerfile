############################
# Build stage
############################
FROM --platform=$BUILDPLATFORM gradle:8.10-jdk23 AS build
WORKDIR /app

# ---- 1) Copy only Gradle files first (dependency cache layer) ----
COPY src/gradlew src/gradlew
COPY src/gradle src/gradle
COPY src/settings.gradle.kts src/settings.gradle.kts
COPY src/build.gradle.kts src/build.gradle.kts
COPY src/apps src/apps

# Pre-download dependencies (THIS IS THE MAGIC)
RUN --mount=type=cache,target=/home/gradle/.gradle \
    cd src && ./gradlew dependencies --no-daemon || true

# ---- 2) Copy the rest of the source (won't bust dependency cache) ----
COPY . .

ARG APP

RUN --mount=type=cache,target=/home/gradle/.gradle \
    cd src && ./gradlew :apps:$APP:bootJar --no-daemon

############################
# Runtime stage
############################
FROM sapmachine:21.0.5-jre-ubuntu-focal AS runner

ARG APP
ARG VERSION

WORKDIR /app
COPY --from=build /app/src/apps/$APP/build/libs/$APP-$VERSION.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
