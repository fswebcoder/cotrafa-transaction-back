# Backend - Cotrafa Transaccional

Este proyecto es el backend para la prueba técnica de Cotrafa, desarrollado con **Java 17** y **Spring Boot 3.2.3**, siguiendo una **Arquitectura Hexagonal**.

## Requisitos Previos

*   Java 17 (JDK)
*   Maven 3.8+

## Cómo Ejecutar la Aplicación

1.  Abre una terminal en la carpeta `backend`.
2.  Ejecuta el siguiente comando:

    ```bash
    mvn spring-boot:run
    ```

3.  La aplicación iniciará en el puerto **8080**.

## Base de Datos (H2)

El proyecto utiliza una base de datos en memoria **H2** para facilitar la ejecución y pruebas sin necesidad de instalar servidores externos.

### Acceso a la Consola de Base de Datos

1.  Asegúrate de que la aplicación esté corriendo.
2.  Abre tu navegador y ve a: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
3.  Ingresa los siguientes datos de conexión:

    *   **Driver Class:** `org.h2.Driver`
    *   **JDBC URL:** `jdbc:h2:mem:testdb`
    *   **User Name:** `sa`
    *   **Password:** `password`

4.  Haz clic en **Connect**.

## Usuarios de Prueba (Data Seeder)

Al iniciar la aplicación, se crea automáticamente un usuario administrador para pruebas:

*   **Usuario:** `admin`
*   **Contraseña:** `admin123`

## Endpoints Principales

### Autenticación (Login)

*   **URL:** `POST /api/auth/login`
*   **Body:**
    ```json
    {
        "username": "admin",
        "password": "admin123"
    }
    ```
*   **Respuesta:** Retorna un Token JWT y la información del usuario.

### Health Check

*   **URL:** `GET /api/ping`
*   **Respuesta:** `Pong! Backend is running correctly.`

---

## Estructura del Proyecto (Arquitectura Hexagonal)

*   `domain`: Lógica de negocio pura (Modelos, Puertos). Independiente de frameworks.
*   `application`: Casos de uso y servicios que orquestan el dominio.
*   `infrastructure`: Adaptadores para interactuar con el mundo exterior (Controladores Web, Base de Datos, Configuración).
