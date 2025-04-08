# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean install

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/book-manager-application/target/book-manager-application-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]