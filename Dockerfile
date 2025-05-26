# Stage 1: Build
FROM openjdk:24-jdk AS java21

RUN mkdir /maven
WORKDIR /maven

# Install Maven manually (latest stable)
RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz | tar xz --strip-components=1

ENV MAVEN_HOME=/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

COPY . .

RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:24-jre

COPY --from=java21 /maven/target/backendSystem-0.0.1-SNAPSHOT.jar demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "demo.jar"]
