FROM openjdk:17 as builder

WORKDIR /app

COPY . /app

RUN chmod +x ./gradlew && ./gradlew build

FROM openjdk:17

WORKDIR /app

COPY build/libs/statement-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]