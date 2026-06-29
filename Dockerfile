# --- Etapa 1: Compilación ---
FROM maven:3.9.5-eclipse-temurin-17 AS builder
WORKDIR /app
# Copiamos el pom.xml y descargamos dependencias en caché
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copiamos el código fuente y compilamos el JAR (omitiendo tests para ir más rápido)
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Imagen de Ejecución Ligera ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos el JAR compilado de la etapa anterior
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]