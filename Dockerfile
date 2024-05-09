FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
WORKDIR /app
COPY . .
RUN apk add --no-cache maven
RUN mvn clean package -DskipTests
ENTRYPOINT ["java","-jar","target/AccessKeyManager-0.0.1-SNAPSHOT.jar"]
