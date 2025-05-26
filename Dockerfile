FROM maven:3.5.8-openjdk-17 AS builder

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim

COPY --from=builder /target/backendSystem-0.0.1-SNAPSHOT.jar demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "demo.jar"]
