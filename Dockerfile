FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -Dmaven.test.skip=true


FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/demo.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]
