ARG BUILD_HOME=/test

FROM openjdk:17 AS build

ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

COPY --chown=gradle:gradle build.gradle settings.gradle $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src
COPY --chown=gradle:gradle config $APP_HOME/config

RUN chmod +x gradlew
RUN ./gradlew clean build --no-daemon

FROM openjdk:17

COPY --from=build $APP_HOME/build/libs/database-1.0-SNAPSHOT-plain.jar /database.jar

EXPOSE 8086

ENTRYPOINT java -jar database.jar