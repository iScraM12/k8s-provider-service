# Car Service API

This project is a simple RESTful service for managing a collection of cars, built with Quarkus.

It provides a full CRUD (Create, Read, Update) API for `Car` entities and is configured to run with a MariaDB database.

## Core Technologies

- **Quarkus**: Supersonic Subatomic Java Framework
- **JAX-RS (REST)**: For building the REST API
- **Hibernate ORM with Panache**: For data persistence
- **MariaDB**: The relational database
- **Lombok**: To reduce boilerplate code
- **JUnit 5 & Mockito**: For unit and integration testing

## API Endpoints

The following endpoints are available under the base path `/cars`:

- `GET /cars`: Retrieves a list of all cars.
- `GET /cars/{id}`: Retrieves a single car by its ID.
- `POST /cars`: Creates a new car.
- `PUT /cars/{id}`: Updates an existing car by its ID.

The API is documented via OpenAPI and can be explored through the **Swagger UI** at `/q/swagger-ui` when the application is running in dev mode.

## Running the Application

### Development Mode

In development mode, the application uses Quarkus Dev Services to automatically start and configure a MariaDB container. No local database setup is required.

To start the application in dev mode, run:

```shell
mvn quarkus:dev
```

You can also use the alias `q` if you have configured it in your `.bashrc`.

### Production Mode

In production, the application is configured to connect to an external database. The database connection details must be provided at runtime.

**1. Start the database:**

Use the provided `docker-compose.yml` to start the MariaDB container:

```shell
docker-compose up -d
```

**2. Run the application:**

Build the application and run it, providing the database credentials as system properties:

```shell
# Build the application
mvn package

# Run the application
java -Dquarkus.profile=prod \
     -Dquarkus.datasource.username=user \
     -Dquarkus.datasource.password=password \
     -Dquarkus.datasource.jdbc.url=jdbc:mariadb://localhost:3306/mydatabase \
     -jar target/quarkus-app/quarkus-run.jar
```

## Running Tests

To run the full test suite (both integration and unit tests), use:

```shell
mvn test
```
