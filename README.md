# 📦 Mareo Envíos - API REST
WebService desarrollado en **Java 17** y **Spring Boot 3** para la gestión, seguimiento y reportes de envíos de mercadería para la empresa **Mareo Envíos**.
---
## 📋 Requisitos Previos
*   Java 17 (si se ejecuta localmente)
*   Docker y Docker Compose
*   Maven 3.x (o utilizar el `./mvnw` incluido)
---
## 🛠️ Cómo Ejecutar el Proyecto
### Opción A: Ejecución con Docker Compose (Recomendado)
1.  Asegúrate de que el archivo `.env` en la raíz tenga las credenciales de base de datos deseadas.
2.  Compila y levanta todo el stack (Aplicación + PostgreSQL + Redis) ejecutando en la raíz del proyecto:
    ```bash
    docker compose up --build
    ```
3.  La aplicación estará disponible y lista en `http://localhost:8080`.
### Opción B: Ejecución Local en Desarrollo (IDE)
1.  Levanta únicamente la base de datos y Redis mediante Docker:
    ```bash
    docker compose up db redis
    ```
2.  Ejecuta la aplicación desde tu IDE o consola usando el comando:
    ```bash
    ./mvnw spring-boot:run
    ```
---
## 🧪 Ejecución de Pruebas (Tests)
El proyecto incluye un set completo de pruebas unitarias aisladas y pruebas de integración sobre base de datos H2 en memoria.
Para correr todos los tests del proyecto:
```bash
./mvnw clean test
```
---
## 📖 Documentación de la API (Swagger UI)
Con la aplicación en ejecución, puedes acceder a la consola interactiva de Swagger para probar los endpoints y visualizar los esquemas JSON de peticiones y respuestas:
🔗 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
---
## 🔌 Ejemplos de Peticiones (Endpoints Principales)
### 1. Crear Solicitud de Envío (Cliente Existente)
*   **POST** `/shipping/create`
*   **Body:**
```json
{
  "customerId": 1,
  "priority": 1,
  "partialFulfillment": false,
  "products": [
    {
      "productId": 1,
      "count": 3
    }
  ]
}
```
### 2. Crear Solicitud de Envío (Cliente Nuevo)
*   **POST** `/shipping/create`
*   **Body:**
```json
{
  "customerId": null,
  "firstName": "Gaston",
  "lastName": "Gomez",
  "address": "Calle San Martin 123",
  "city": "Mendoza",
  "priority": 1,
  "products": [
    {
      "productId": 2,
      "count": 1
    }
  ]
}
```
### 3. Transicionar Estado (Ej: En camino)
*   **POST** `/shipping/transition/inTravel/{shippingId}`
### 4. Obtener Reporte de Productos más Solicitados (Top Dinámico)
*   **GET** `/reports/topSended?limit=3`