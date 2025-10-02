# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiar el JAR generado desde la etapa build
COPY --from=build /app/target/finanzas_app-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto (informativo)
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
