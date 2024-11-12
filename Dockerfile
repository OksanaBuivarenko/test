ARG BUILD_HOME=/test

FROM openjdk:17 AS build

ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

COPY ./build.gradle.kts .
COPY ./settings.gradle.kts .
COPY ./gradlew .
COPY ./src .
COPY ./config .

RUN chmod +x gradlew
RUN ./gradlew clean build --no-daemon

FROM openjdk:17

COPY --from=build $APP_HOME/build/libs/database-1.0-SNAPSHOT-plain.jar /database.jar

EXPOSE 8086

ENTRYPOINT java -jar database.jar