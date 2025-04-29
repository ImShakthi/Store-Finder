# Store Finder

**Store Finder** is a service that manages and provides detailed information about Jumbo stores,
including operating hours, geolocation, address, collection point status, and location type. It also
supports geospatial queries to identify stores near a specific coordinate within a defined radius.

## 🌟 Core Features

- Find the top `N` nearby stores based on:
    - Input `longitude`, `latitude`
    - Optional `radius` (e.g., `800m`, `10km`)
- Retrieve a list of stores with optional filters by:
    - `city`
    - `collectionPoint`
    - `storeLocationType`
    - `openNow`
- Add, update, or delete store records (secured via JWT-based authentication)
- Fetch lists of:
    - Available cities
    - Store location types
    - Address information
- Developer tooling:
    - Interactive API documentation via *
      *[Swagger UI](https://jumbo-store-api-f1ae1b3de689.herokuapp.com/swagger-ui/index.html)**
    - **Liquibase** for database version control and migration

## Techstack used👾

- Java 21
- Spring Boot 3
- Maven 3.9
- Docker
- docker-compose
- Github Actions
- Liquibase
- MapStruct
- Postgres with Postgis
- Redis
- JWT
- Jasypt
- Junit 5
- TestContainers
- Heroku
- swagger
- postman

## 🚀 How to Run the Project

### 🔧 Prerequisites

Ensure the following tools are installed:

- Java 21
- Apache Maven 3.9
- Docker
- Docker Compose
- [Jasypt CLI](https://github.com/jasypt/jasypt/releases/download/jasypt-1.9.3/jasypt-1.9.3-dist.zip)
  Tool (Only
  required if modifying encrypted properties)

---

### 📁 Environment Variable Setup

The application relies on environment variables for configuration. You can set them in a .env file
at the project root.

📝 Example .env file

``` env
export SPRING_DATASOURCE_DATABASE=storeapi
export SPRING_DATASOURCE_URL=jdbc:postgresql://postgis:5432/${SPRING_DATASOURCE_DATABASE}
export SPRING_DATASOURCE_USERNAME=user
export SPRING_DATASOURCE_PASSWORD=password

export JASYPT_ENCRYPTOR_PASSWORD=jasypt-encrypt-passwd

export SPRING_REDIS_HOST=redis
export SPRING_REDIS_PORT=6379

```

_⚠️ Make sure to keep sensitive keys (like JASYPT_ENCRYPTOR_PASSWORD) secure and private. Never
commit them to version
control._

---

### 📦 Setup and Run

This application is Dockerized using docker-compose.yml, bundling the backend, MySQL, and Redis.

1. Clone the repository

```bash
git clone git@github.com:ImShakthi/Store-Finder.git
cd Store-Finder
```

2. Build the application using Maven

```bash
./mvnw clean package
```

3. Build and start the server with Docker Compose, export all environment variables

```bash
docker-compose up --build
```

4. Access Swagger UI to explore available APIs:

```
http://localhost:8080/swagger-ui/index.html
```

---

### ⚙️ CI/CD

CI/CD is handled via GitHub Actions and Heroku.

- CI/CD Pipeline = [GitHub Actions](https://github.com/ImShakthi/Store-Finder/actions)

- Live Deployment (Heroku)
  https://jumbo-store-api-f1ae1b3de689.herokuapp.com/swagger-ui/index.html

*

*_📝 Note: The Heroku server is hosted on a free-tier plan. Expect some delay due to cold starts when
inactive._**

# 🧪 How to Test the Store-Finder Service

Store-Finder supports both unit tests and integration tests. Integration testing is designed to
simulate real-world usage, and includes full journey validation for core features like store
management, store details, and authentication.

The integration test class `com.skthvl.storeapi.StoreApiApplicationTests` demonstrates a complete
user journey.

----

## ✅ Run All Tests

```bash
./mvnw clean test
```

## 🧱 Integration Testing Stack

- ✅ [Testcontainers](https://www.testcontainers.org/) – Spins up isolated Postgres and Redis
  containers during integration tests

_These tools ensure tests are environment-independent and do not rely on external services or shared
databases._

## 🚀 Run the Server Locally

To run the app locally, build the project and start the services using Docker Compose:

```bash
./mvnw clean package
docker-compose up --build
```

## 📬 Test the API

You can test the REST APIs via:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Postman Collection: Import from the file: `stores-api.postman_collection.json`
