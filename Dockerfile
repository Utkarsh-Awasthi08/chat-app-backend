# Step 1: Use official JDK 21 as builder
FROM eclipse-temurin:21-jdk AS builder

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy Maven wrapper and pom.xml
COPY mvnw* pom.xml ./
COPY .mvn .mvn

# Step 4: Download dependencies (better caching)
RUN ./mvnw dependency:go-offline

# Step 5: Copy source code
COPY src src

# Step 6: Build the application
RUN ./mvnw clean package -DskipTests

# Step 7: Use a lightweight JRE for runtime
FROM eclipse-temurin:21-jre-alpine

# Step 8: Set working directory
WORKDIR /app

# Step 9: Copy built jar from builder
COPY --from=builder /app/target/chat-app-0.0.1-SNAPSHOT.jar app.jar

# Step 10: Expose port 8080
EXPOSE 8080

# Step 11: Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]