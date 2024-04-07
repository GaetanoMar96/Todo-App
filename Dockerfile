# Use OpenJDK 17 as a base image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper and build scripts
COPY gradlew .
COPY gradle ./gradle

# Copy the build configuration files
COPY build.gradle .
COPY settings.gradle .

# Download dependencies first so they get cached in Docker layer
RUN ./gradlew dependencies

# Copy the application source code
COPY src ./src

# Build the application
RUN ./gradlew build

# Expose the port your Spring Boot application runs on
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "build/libs/todo-0.0.1-SNAPSHOT.jar"]