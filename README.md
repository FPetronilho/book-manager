# Book Manager Microservice

The Book Manager microservice is part of the Tracktainment application, which is designed to track books, movies, and games consumed by users. This microservice is responsible for managing books and their associated metadata. It integrates with the Dux Manager microservice to manage digital user assets.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [API Endpoints](#api-endpoints)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
  - [Building](#building)
  - [Running Locally](#running-locally)
  - [Docker Setup](#docker-setup)
- [Error Handling](#error-handling)
- [Integration with DuxManager](#integration-with-duxmanager)
- [Validation](#validation)
- [Development](#development)
  - [Tech Stack](#tech-stack)
  - [Project Structure](#project-structure)
  - [Next Features](#next-features)
- [Potential Tracktainment Upgrades](#potential-tracktainment-upgrades)

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
- Docker (optional, for containerized deployment)

### Configuration

Create an `application.properties` or `application.yml` file with the following properties:

```properties
# Database configuration
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/book-manager
    username: postgres
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update

# DuxManager service URL
http:
  url:
    dux-manager: http://dux-manager:8080/dux-manager/api/v1
```
> Note: When running the application in Docker, the http.url.dux-manager property must use the service name (dux-manager) as the hostname because both services (book-manager and dux-manager) are part of the same Docker network. 

### Building

To build the application, run the following command:
```bash
mvn clean package
```

### Running Locally

To run the application locally, use the following command:
```bash
java -jar book-manager.jar
```
Ensure that the http.url.dux-manager property in the application.yml file points to the correct URL where the dux-manager service is running. For example:
- If dux-manager is running locally:
```
http:
  url:
    dux-manager: http://localhost:8080/dux-manager/api/v1
```

### Docker Setup
 
The book-manager application can now be containerized using Docker. To run the application in Docker, follow these steps:
  - Step 1: Create docker network -  As Book Manager depends that Dux Manager is up and running, create a docker network so that both microservices can communicate. Run the following command:
```
docker network create shared-network
```
  - Step 2: Build and run Dux Manager - Link for : https://github.com/FPetronilho/dux-manager
  - Step 3: Configure the Dux Manager Service - Ensure application.yaml is updated to work on the same network inside docker:
 ```
 http:
   url:
     dux-manager: http://dux-manager:8080/dux-manager/api/v1
 ```
 - Step 4: Build the Docker Image - Run the following command to build the Docker image:
 ```
 docker-compose up --build
 ```
 
  - Step 5: Start the Containers - Start the containers using the following command:
 ```
 docker-compose up
 ```
 
 The book-manager service will be accessible at http://localhost:8081.
 
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
- PostgreSQL
- Maven
- Docker
- OAuth2/JWT authentication
- Swagger documentation
- HTTPS enabled via SSL certificates

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

### Next Features 

- Unit testing;
- Database encryption;
- CI/CD pipeline.

## Potential Tracktainment Upgrades

- **Review Microservice**: A microservice to handle reviews of books, games and movies.
- **Recommendation Microservice**: A microservice to handle books, games and movies recommendations based on what the user has consumed so far.
- **Notification Microservice** : A microservice to send notifications to users about recommendations.
