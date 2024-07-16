FROM gradle:7.3.3-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/com.abc.ktor-chat-0.0.1-all.jar /app/abc-main.jar
ENTRYPOINT ["java", "-jar", "/app/abc-main.jar"]
