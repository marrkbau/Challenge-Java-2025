FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw && ./mvnw clean install -DskipTests=true

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/challenge-0.0.1-SNAPSHOT.jar /app/challenge.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/challenge.jar"]
