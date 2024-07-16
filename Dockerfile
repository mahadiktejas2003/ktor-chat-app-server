FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/*-all.jar /app/abc-main.jar
ENTRYPOINT ["java", "-jar", "/app/abc-main.jar"]