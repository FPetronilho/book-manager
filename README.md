# Book Manager Microservice

The Book Manager microservice is part of the Tracktainment application, which is designed to track books, movies, and games consumed by users. This microservice is responsible for managing books and their associated metadata. It integrates with the Dux Manager microservice to manage digital user assets.

## Overview

The Book Manager microservice provides CRUD (Create, Read, Update, Delete) operations for managing books. It adheres to the principles of Clean Architecture , ensuring modularity, scalability, and maintainability. The service interacts with a PostgreSQL database for persistent storage and communicates with the Dux Manager microservice via REST APIs to manage digital user assets.

## Architecture

The project follows a clean architecture with clear separation of concerns:

- **Application Module**: Handles application configuration and properties.
- **Core Module**: Contains domain models, DTOs, use cases, and interfaces for data providers
- **Data Provider SQL Module**: Implementation of persistence layer using JPA/Hibernate
- **Entry Point REST Module**: REST API controllers and exception handling
- **Data Provider REST Module**: Integration with external DuxManager service

## Features

- Complete CRUD operations for book entities
- Advanced filtering and search capabilities
- Sorting by various book attributes
- Integration with DuxManager for asset tracking
- Comprehensive validation and error handling

## API Endpoints

| Method |       Endpoint       |       Description       |
|--------|----------------------|-------------------------|
| POST   | `/api/v1/books`      | Create a new book       |
| GET    | `/api/v1/books/{id}` | Get a book by ID        |
| GET    | `/api/v1/books`      | List books with filters |
| PATCH  | `/api/v1/books/{id}` | Update a book           |
| DELETE | `/api/v1/books/{id}` | Delete a book           |

## Data Model

The Book entity has the following attributes:

- `id`: Unique identifier
- `title`: Book title
- `author`: Book author
- `isbn`: International Standard Book Number
- `publisher`: Book publisher
- `publishedDate`: Date of publication
- `language`: Book language
- `createdAt`: Record creation timestamp
- `updatedAt`: Last update timestamp

## Getting Started

### Prerequisites

- Java 17+
- Maven
- PostgreSQL or another compatible database
- Access to DuxManager service

### Configuration

Create an `application.properties` or `application.yml` file with the following properties:

```properties
# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/book-manager
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# DuxManager service URL
http.url.dux-manager=http://localhost:8080/dux-manager/api/v1
```

### Building

```bash
mvn clean package
```

### Running

```bash
java -jar book-manager.jar
```

## Error Handling

The service provides structured error responses with the following format:

```json
{
  "code": "E-002",
  "httpStatusCode": 404,
  "reason": "Resource not found.",
  "message": "Book your-book-id not found."
}
```

Common error codes:
- `E-001`: Internal server error
- `E-002`: Resource not found
- `E-003`: Resource already exists
- `E-007`: Parameter validation error

## Integration with DuxManager

The Book Manager service integrates with DuxManager for asset tracking. Each book created in the system is also registered as an asset in DuxManager with the following attributes:
- `externalId`: Book ID
- `type`: "book"
- `permissionPolicy`: "owner"
- `artifactInformation`: Contains group, artifact, and version details

## Validation

The service includes comprehensive validation for all inputs:
- Book title and author validation
- ISBN format validation
- Date format validation
- Query parameter validation

## Development

### Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Jakarta Validation
- Feign Client
- Lombok
- MapStruct
- Logging with SLF4J
- OkHttp
- PostgreSQL
- Maven

### Project Structure

```
com.tracktainment.bookmanager
├── api                    # API interfaces
├── client                 # External service clients
├── config                 # Configuration classes
├── controller             # REST controllers
├── dataprovider           # Data provider implementations
├── domain                 # Domain models
├── dto                    # Data Transfer Objects
├── entity                 # JPA entities
├── exception              # Exception handling
├── mapper                 # Object mappers
├── repository             # Data repositories
├── security               # Security context
├── usecases               # Business logic implementation
└── util                   # Utility classes
```
