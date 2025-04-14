

# Sistema Educativo Distribuido con Spring Boot y DevOps

Este proyecto implementa un sistema educativo basado en **arquitectura de microservicios**, desarrollado con **Spring Boot**, que incluye:

- Autenticación con **JWT**
- Pruebas automatizadas con **JUnit**, **Mockito** y **WebTestClient**
- Configuración centralizada con **Spring Cloud Config Server**
- Descubrimiento de servicios con **Eureka**
- **Contenerización** con Docker
- **Automatización CI/CD** con GitHub Actions

---

## Microservicios del sistema

| Microservicio          | Puerto | Funcionalidad                                |
|------------------------|--------|----------------------------------------------|
| `usuarios-servicio`    | 8081   | Registro, login, autenticación JWT           |
| `asignaturas-servicio` | 8082   | CRUD de materias/asignaturas                 |
| `matriculas-servicio`  | 8083   | Registro de matrículas (con Feign)           |
| `config-server`        | 8888   | Centraliza la configuración (GitHub)         |
| `eureka-server`        | 8761   | Descubrimiento de servicios (Eureka)         |

---

## Arquitectura general

El sistema sigue la arquitectura de microservicios, donde cada módulo funciona de forma independiente y se comunica a través de HTTP o Feign.

- Todos los servicios se **registran en Eureka**
- Usan configuración externa desde **Config Server**
- `matriculas-servicio` se comunica con `usuarios-servicio` y `asignaturas-servicio` usando **OpenFeign**
- El sistema se ejecuta con **Docker Compose**

 Diagrama general (puedes insertar aquí un diagrama de arquitectura tipo UML o de componentes)

---

## Seguridad con JWT

Implementada en `usuarios-servicio` con **Spring Security + JWT**.

### Flujo de autenticación:

1. `POST /auth/register` → Registro del usuario
2. `POST /auth/login` → Retorna un token JWT
3. Usar el token en las peticiones:


Los endpoints de los demás servicios pueden validarse usando el mismo token.

---

## Pruebas automatizadas

- **JUnit + Mockito** para pruebas unitarias
- **WebTestClient** para pruebas de integración
- Se ejecutan automáticamente en **GitHub Actions**

### Ejemplo de GitHub Action (`.github/workflows/test.yml`):


name: Pruebas Automatizadas

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3
      - name: Configurar JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Ejecutar pruebas
        run: mvn test

----
##  Configuración centralizada con Spring Cloud Config Server
Los microservicios no usan application.properties local, sino que se conectan a config-server, el cual obtiene sus propiedades desde un repositorio en GitHub (sistema-config).

## Ejemplo de bootstrap.properties en usuarios-servicio:
spring.application.name=usuarios-servicio
spring.config.import=optional:configserver:http://localhost:8888

## Ejemplo de archivo en GitHub: usuarios-servicio.properties
server.port=8081
spring.application.name=usuarios-servicio
eureka.client.service-url.defaultZone=http://localhost:8761/eureka

---

## Descubrimiento de servicios con  Eureka

1. eureka-server se ejecuta en el puerto 8761
2. Cada microservicio se registra automaticamente en Eureka
3. Dashboard disponible en:
👉 http://localhost:8761
---

## Contenerización con Docker y Docker Compose
Cada microservicio contiene un Dockerfile como el siguiente:

FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
Se incluye un archivo docker-compose.yml en la raíz del proyecto para levantar todo el sistema con un solo comando:

services:
  config-server:
    build: ./config-server
    ports:
      - "8888:8888"

  eureka-server:
    build: ./eureka-server
    ports:
      - "8761:8761"
    depends_on:
      - config-server

  usuarios-servicio:
    build: ./usuarios-servicio
    ports:
      - "8081:8081"
    depends_on:
      - eureka-server
      - config-server

  asignaturas-servicio:
    build: ./asignaturas-servicio
    ports:
      - "8082:8082"
    depends_on:
      - eureka-server
      - config-server

  matriculas-servicio:
    build: ./matriculas-servicio
    ports:
      - "8083:8083"
    depends_on:
      - eureka-server
      - config-server

---

## 🧠 Tecnologías utilizadas
## Tecnología	Uso principal
1. Java 17	Lenguaje de desarrollo principal
2. Spring Boot	Framework base de los microservicios
3. Spring Security	Seguridad y autorización con JWT
4. Spring Cloud Config	Configuración externa centralizada
5. Spring Cloud Eureka	Descubrimiento de servicios
6. OpenFeign	Comunicación entre microservicios
7. Docker	Contenerización de servicios
8. Docker Compose	Orquestación de contenedores
9. GitHub Actions	Automatización de pruebas CI
10. H2 Database	Base de datos en memoria para desarrollo
11. Maven	Sistema de construcción y dependencias

---
## 🚀 Cómo ejecutar el sistema completo
Asegúrate de tener Docker instalado

Ejecuta los siguientes comandos en la raíz del proyecto:

mvn clean package -DskipTests
docker-compose up --build
Accede a:

Config Server: http://localhost:8888

Eureka Server: http://localhost:8761

Microservicios: puertos 8081 a 8083

---

## ✍️ Autor
## 📚 Angelly Ordoñez
## 💡 Proyecto académico de Microservicios y DevOps

## 📌 Notas finales
Este sistema fue desarrollado con fines académicos. Se recomienda continuar con:
Migración a PostgreSQL o MongoDB
Despliegue en la nube (Heroku, VPS, Render)
Métricas y trazabilidad (Actuator + Zipkin)
Control de roles y permisos por usuario















