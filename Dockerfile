FROM openjdk:17 AS build

WORKDIR /app

COPY ./gradlew .
COPY ./gradle/wrapper ./gradle/wrapper
COPY ./build.gradle.kts .
COPY ./settings.gradle.kts .
COPY ./ ./

RUN chmod +x gradlew
RUN ./gradlew clean build --no-daemon

FROM openjdk:17

COPY --from=build /app/build/libs/database-1.0-SNAPSHOT-plain.jar /database.jar

EXPOSE 8086

CMD ["java", "-jar", "/app/database.jar"]