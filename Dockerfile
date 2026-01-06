FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="poyrazemun"
WORKDIR /usr/app
COPY target/quizzapp-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]