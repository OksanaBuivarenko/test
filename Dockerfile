FROM openjdk:17 AS build


WORKDIR /test

COPY ./build.gradle.kts .
COPY ./settings.gradle.kts .
COPY ./gradlew .
COPY ./src .
COPY ./config .

#RUN chmod +x gradlew
#RUN ./gradlew clean build --no-daemon
RUN gradle --no-daemon build

FROM openjdk:17

COPY --from=build-image /test/build/libs/database-1.0-SNAPSHOT-plain.jar /database.jar

ENTRYPOINT java -jar database.jar