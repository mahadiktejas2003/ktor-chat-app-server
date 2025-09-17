# ---- Build Stage ----
FROM gradle:8.4.0-jdk20 AS build
WORKDIR /app
COPY . .
RUN gradle shadowJar --no-daemon

# ---- Run Stage ----
FROM openjdk:20-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/com.abc.ktor-chat-all.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]