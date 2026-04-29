FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=9090"]
