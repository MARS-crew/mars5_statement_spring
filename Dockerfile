FROM openjdk:17

COPY build/libs/statement-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /app/log

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=dev
ENV TZ=Asia/Seoul
ENV JAVA_OPTS="-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dfile.encoding=UTF-8"

ENTRYPOINT ["java", "-jar", "app.jar"]