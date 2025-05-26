FROM maven:3.9.6-eclipse-temurin-17 AS builder

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

COPY --from=builder /target/backendSystem-0.0.1-SNAPSHOT.jar demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "demo.jar"]
