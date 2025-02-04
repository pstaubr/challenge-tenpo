# Tenpo API

## Descripción del Proyecto

Tenpo API es una aplicación de Spring Boot desarrollada en Java 21 que proporciona una API REST para realizar cálculos
con porcentajes dinámicos, almacenar el historial de llamadas y controlar la tasa de solicitudes. La aplicación utiliza
PostgreSQL como base de datos y emplea HikariCP para la gestión de conexiones.

## Funcionalidades Principales

1. **Cálculo con porcentaje dinámico**:
    - Implementa un endpoint REST que recibe dos números (`num1` y `num2`).
    - Suma ambos números y aplica un porcentaje adicional obtenido de un servicio externo (mock) que retorna un valor
      fijo (por ejemplo, 10%).

2. **Caché del porcentaje**:
    - El porcentaje obtenido del servicio externo se almacena en memoria (caché) y se considera válido durante 30
      minutos.
    - Si el servicio externo falla, se usa el último valor almacenado en caché. Si no hay un valor en caché, la API
      responde con un error HTTP adecuado.

3. **Reintentos ante fallos del servicio externo**:
    - Si el servicio externo falla, se implementa una lógica de reintento con un máximo de 3 intentos antes de devolver
      un error o usar el valor en caché.

4. **Historial de llamadas**:
    - Implementa un endpoint para consultar un historial de todas las llamadas realizadas a los endpoints de la API.
      Incluye detalles como fecha, hora, endpoint invocado, parámetros recibidos, y respuesta o error retornado.

5. **Control de tasas (Rate Limiting)**:
    - La API soporta un máximo de 3 RPM (solicitudes por minuto). Si se excede este umbral, responde con un error HTTP
      429 (Too Many Requests) y un mensaje descriptivo.

6. **Manejo de errores HTTP**:
    - Implementa manejo adecuado de errores HTTP para las series 4XX y 5XX, con mensajes descriptivos para ayudar a los
      clientes a entender el problema.

## Instrucciones para Ejecutar el Servicio y la Base de Datos Localmente

### Prerrequisitos

- [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/download/)
- [Docker](https://www.docker.com/products/docker-desktop)

### Configuración de la Base de Datos

1. Instala y configura PostgreSQL en tu máquina local.
2. Crea una base de datos llamada `postgres`:
   ```sql
   CREATE DATABASE postgres;

Configura las credenciales de la base de datos en el archivo application.properties:
spring.datasource.url=jdbc:postgresql://localhost:5432/tu_bd
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=create
spring.cache.type=simple

Ejecutar el Servicio
Clona el repositorio:
git clone https://github.com/pstaubr/challenge-tenpo.git
cd challenge-tenpo

Compila y ejecuta el proyecto con Maven:
mvn clean install
mvn spring-boot:run

Interactuar con la API
Puedes interactuar con la API utilizando herramientas como Postman o cURL.
Endpoints Principales
Cálculo con Porcentaje Dinámico:
URL: /api/dynamicCalculation
Método: POST
Parámetros: num1 (double), num2 (double)
Ejemplo de Solicitud:
curl -X POST "http://localhost:8080/api/dynamicCalculation?num1=5&num2=5"
Consultar Historial de Llamadas:
URL: /api/callHistory
Método: GET
Parámetros: page (int, opcional), size (int, opcional)
Ejemplo de Solicitud:
curl -X GET "http://localhost:8080/api/callHistory?page=0&size=10"

### Construir y publicar la imagen Docker:

docker compose up -d --build
Levantar los servicios usando Docker Compose:
mvn clean install
docker build -t pstaubr/challengetenpo .
docker tag pstaubr/challengetenpo pstaubr/challengetenpo:lastest
docker push pstaubr/challengetenpo:lastest
Verificar que los contenedores estén corriendo:
docker ps

### Detalles sobre cómo interactuar con la API

Endpoints
POST /api/dynamicCalculation
Parámetros:
num1: Número 1 (double)
num2: Número 2 (double)
Respuesta: Resultado del cálculo
GET /api/callHistory
Parámetros:
page: Número de página (opcional)
size: Tamaño de la página (opcional)
Respuesta: Historial de llamadas

### Enlace al Docker Hub

https://hub.docker.com/r/pstaubr/challengetenpo

### Paso 6: Probar la Configuración

Para verificar que todo está funcionando correctamente:

1. **Ejecutar imagen**:
   ```sh
   docker-compose up -d

2. **Asegúrate de que los contenedores estén corriendo**:
   ```sh
   docker ps   

Envía solicitudes a la API desde Postman:

POST: http://localhost:8080/api/DynamicCalculation

GET:  http://localhost:8080/api/callHistory