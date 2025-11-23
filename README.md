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
- `DELETE /cars/{id}`: Deletes a car by its ID.

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

**2. Run the application (JAR):**

Build the application and run it, providing the database credentials as system properties:

```shell
# Build the application
mvn package

# Run the application
java -Dquarkus.profile=prod \
     -Dquarkus.datasource.username=user \
     -Dquarkus.datasource.password=password \
     -Dquarkus.datasource.jdbc.url=jdbc:mariadb://mariadb:3306/mydatabase \
     -jar target/quarkus-app/quarkus-run.jar
```

**3. Run the application (Docker Container):**

If you want to run your application as a separate Docker container while still using the `docker-compose` managed MariaDB:

First, build your Quarkus application Docker image:

```shell
mvn clean package -Dnative
```

Then, identify the Docker network created by `docker-compose`. It's usually named `[your-project-directory-name]_default`. For this project, it would be `k8s-provider-service_default`.

Finally, run your application container, connecting it to that network:

```shell
docker run -it --rm \
  --network k8s-provider-service_default \
  -p 8080:8080 \
  -e QUARKUS_PROFILE=prod \
  -e QUARKUS_DATASOURCE_USERNAME=user \
  -e QUARKUS_DATASOURCE_PASSWORD=password \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:mariadb://mariadb:3306/mydatabase \
  k8s-consumer-service:1.0.0-SNAPSHOT # Adjust image name and tag if necessary
```

## Running Tests

To run the full test suite (both integration and unit tests), use:

```shell
mvn test
```
