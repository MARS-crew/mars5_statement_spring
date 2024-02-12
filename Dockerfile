FROM openjdk:17

COPY build/libs/statement-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /app/log

ENTRYPOINT ["java", "-jar", "app.jar"]