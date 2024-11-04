FROM openjdk:21-jdk AS build
WORKDIR /

RUN microdnf install findutils

COPY --chown=gradle:gradle . .
RUN ./gradlew bootJar --no-daemon -PjarName=AppieCal

FROM sapmachine:21-jre-ubuntu-focal AS runner

WORKDIR /appiecal
COPY --from=build ./build/libs/AppieCal.jar ./AppieCal.jar

LABEL authors="Daan Klarenbeek"
EXPOSE 8080
RUN mkdir ./data

ENTRYPOINT ["java", "-jar", "AppieCal.jar"]