FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/finanzas_app-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","app.jar"]
