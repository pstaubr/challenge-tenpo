# Usar una imagen base de OpenJDK 21
FROM openjdk:21-jdk-slim

# Configurar el directorio de trabajo
WORKDIR /app

# Copiar el JAR de la aplicación
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre la API
EXPOSE 8080

# Comando de inicio de la aplicación
CMD ["java", "-jar", "app.jar"]