# Build stage
FROM eclipse-temurin:11-jdk as builder
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn/ .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw package -DskipTests

# Run stage
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /workspace/app/target/*.jar app.jar

# Use exec form to properly expand PORT variable
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=${PORT:-8080}"]