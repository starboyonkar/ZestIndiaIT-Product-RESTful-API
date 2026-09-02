# Design a RESTful API solution around Products with JWT to perform full CRUD operations using Java and Spring Boot

A production-oriented RESTful Product Management API built with **Java and Spring Boot**, providing secure CRUD operations for Products and their associated Items.

The application follows a layered architecture with **Spring Data JPA, Hibernate, MySQL, Spring Security, JWT authentication, refresh-token rotation, role-based authorization, Jakarta Validation, pagination, standardized exception handling, Swagger/OpenAPI documentation, JUnit 5, Mockito, H2 integration testing, and Docker**.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Objectives](#objectives)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Database Design](#database-design)
- [Authentication and Authorization](#authentication-and-authorization)
- [API Base URL](#api-base-url)
- [Authentication Endpoints](#authentication-endpoints)
- [Product Endpoints](#product-endpoints)
- [Item Endpoints](#item-endpoints)
- [Pagination and Sorting](#pagination-and-sorting)
- [Request Validation](#request-validation)
- [Error Handling](#error-handling)
- [Swagger / OpenAPI](#swagger--openapi)
- [Configuration](#configuration)
- [Local Setup](#local-setup)
- [Running with Maven](#running-with-maven)
- [Running with Docker](#running-with-docker)
- [Testing](#testing)
- [Security](#security)
- [Database Indexing](#database-indexing)
- [Environment Variables](#environment-variables)
- [Git and Secret Management](#git-and-secret-management)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

# Project Overview

This project implements a secure and scalable RESTful API for managing Products and their associated Items.

The API supports complete CRUD operations while following REST principles and a consistent versioned URL structure:

```text
/api/v1/
```

The application uses JWT-based authentication and role-based authorization to protect API resources.

The primary relationship is:

```text
Product
   │
   │ 1 : N
   │
   ├── Item
   ├── Item
   └── Item
```

A Product can contain multiple Items, while every Item belongs to exactly one Product.

---

# Objectives

The main objectives of this project are:

- Design a RESTful API around Products.
- Implement complete Product CRUD operations.
- Implement Item management under Products.
- Use Java 17+ and Spring Boot.
- Persist application data using MySQL.
- Use Spring Data JPA and Hibernate for database access.
- Secure APIs using Spring Security and JWT.
- Implement refresh-token rotation.
- Implement role-based authorization.
- Validate incoming requests using Jakarta Validation.
- Provide pagination and sorting.
- Implement standardized API error responses.
- Provide Swagger/OpenAPI documentation.
- Write unit tests using JUnit 5 and Mockito.
- Write integration tests using Spring Boot Test and H2.
- Containerize the application using Docker.
- Run the complete application stack using Docker Compose.

---

# Features

## Product Management

- Create Product
- Retrieve all Products
- Retrieve Product by ID
- Update Product
- Delete Product
- Pagination
- Sorting
- Request validation

## Item Management

- Create Item under Product
- Retrieve Product Items
- Retrieve individual Item
- Update Item
- Delete Item
- Pagination
- Product/Item relationship validation

## Authentication

- User registration
- User login
- JWT access token
- Refresh token
- Refresh token rotation
- Logout / token revocation

## Authorization

- Role-based access control
- USER role
- ADMIN role
- Protected Product and Item operations

## API Quality

- RESTful URL design
- API versioning
- DTO-based request/response model
- Global exception handling
- Standardized error responses
- Jakarta Bean Validation
- Database indexes
- Swagger/OpenAPI documentation

## Development and Deployment

- Maven
- JUnit 5
- Mockito
- Spring Boot Test
- H2 test database
- Docker
- Docker Compose

---

# Technology Stack

| Technology         | Purpose                          |
| ------------------ | -------------------------------- |
| Java 17+           | Programming language             |
| Spring Boot        | Application framework            |
| Spring Web         | REST API development             |
| Spring Security    | Authentication and authorization |
| JWT                | Stateless authentication         |
| Refresh Token      | Session renewal                  |
| Spring Data JPA    | Data access layer                |
| Hibernate          | ORM                              |
| MySQL              | Production database              |
| H2                 | Test database                    |
| Jakarta Validation | Request validation               |
| Lombok             | Boilerplate reduction            |
| Maven              | Build and dependency management  |
| JUnit 5            | Unit testing                     |
| Mockito            | Mock-based testing               |
| Spring Boot Test   | Integration testing              |
| Swagger/OpenAPI    | API documentation                |
| Docker             | Application containerization     |
| Docker Compose     | Multi-container orchestration    |

---

# Architecture

The application follows a layered architecture.

```text
                    Client
                      │
                      ▼
              ┌───────────────┐
              │ Spring Security│
              │ JWT Filter     │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │   Controller  │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │      DTO      │
              │ Validation    │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │    Service    │
              │ Business Logic│
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │  Repository   │
              │ Spring Data   │
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │ JPA / Hibernate│
              └───────┬───────┘
                      │
                      ▼
              ┌───────────────┐
              │     MySQL     │
              └───────────────┘
```

---

# Security Architecture

Authentication is implemented using JWT.

```text
Client
   │
   │ Login
   ▼
Authentication Controller
   │
   ▼
Authentication Manager
   │
   ▼
User Authentication
   │
   ▼
JWT Access Token
   +
Refresh Token
```

For protected requests:

```text
Client
   │
   │ Authorization: Bearer <JWT>
   ▼
JWT Authentication Filter
   │
   ▼
Validate JWT
   │
   ▼
Extract Username + Roles
   │
   ▼
SecurityContext
   │
   ▼
Controller
```

The access token is short-lived, while the refresh token is used to obtain a new access token.

Refresh-token rotation replaces the previous refresh token with a new one during refresh operations.

---

# Project Structure

```text
product-management-api/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/productmanagement/
│   │   │
│   │   │       ├── config/
│   │   │       │   ├── CorsConfig.java
│   │   │       │   └── OpenApiConfig.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── ProductController.java
│   │   │       │   └── ItemController.java
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── auth/
│   │   │       │   ├── product/
│   │   │       │   └── item/
│   │   │       │
│   │   │       ├── entity/
│   │   │       │   ├── User.java
│   │   │       │   ├── RefreshToken.java
│   │   │       │   ├── Product.java
│   │   │       │   └── Item.java
│   │   │       │
│   │   │       ├── exception/
│   │   │       │   ├── ErrorResponse.java
│   │   │       │   ├── FieldErrorResponse.java
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   └── ResourceNotFoundException.java
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   ├── UserRepository.java
│   │   │       │   ├── RefreshTokenRepository.java
│   │   │       │   ├── ProductRepository.java
│   │   │       │   └── ItemRepository.java
│   │   │       │
│   │   │       ├── security/
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   ├── JwtService.java
│   │   │       │   └── SecurityConfig.java
│   │   │       │
│   │   │       └── service/
│   │   │           ├── AuthService.java
│   │   │           ├── RefreshTokenService.java
│   │   │           ├── ProductService.java
│   │   │           └── ItemService.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       ├── java/
│       │   └── com/example/productmanagement/
│       │       ├── controller/
│       │       ├── service/
│       │       └── integration/
│       │
│       └── resources/
│           └── application-test.properties
│
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

# Database Design

The application uses MySQL.

## Product Table

```sql
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    created_on TIMESTAMP NOT NULL,
    modified_by VARCHAR(100),
    modified_on TIMESTAMP
);
```

## Item Table

```sql
CREATE TABLE item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT fk_item_product
        FOREIGN KEY (product_id)
        REFERENCES product(id)
);
```

The relationship is:

```text
product.id
     │
     │
     ▼
item.product_id
```

---

# Authentication and Authorization

The API uses two primary roles:

```text
ROLE_USER
ROLE_ADMIN
```

Typical authorization rules:

| Operation      | USER | ADMIN |
| -------------- | ---: | ----: |
| View Products  |  Yes |   Yes |
| Create Product |   No |   Yes |
| Update Product |   No |   Yes |
| Delete Product |   No |   Yes |
| View Items     |  Yes |   Yes |
| Create Item    |   No |   Yes |
| Update Item    |   No |   Yes |
| Delete Item    |   No |   Yes |

Authentication endpoints are publicly accessible.

All protected endpoints require a valid JWT access token.

---

# API Base URL

For local development:

```text
http://localhost:8086/api/v1
```

---

# Authentication Endpoints

## Register

```http
POST /api/v1/auth/register
```

Creates a new user account.

Example request:

```json
{
  "username": "onkar",
  "password": "Password@123",
  "role": "USER"
}
```

---

## Login

```http
POST /api/v1/auth/login
```

Authenticates a user and returns an access token and refresh token.

Example request:

```json
{
  "username": "onkar",
  "password": "Password@123"
}
```

Example response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
  "tokenType": "Bearer",
  "expiresIn": 900000
}
```

---

## Refresh Token

```http
POST /api/v1/auth/refresh
```

Generates a new access token using a valid refresh token.

Example request:

```json
{
  "refreshToken": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```

The refresh token is rotated after successful refresh.

---

## Logout

```http
POST /api/v1/auth/logout
```

Revokes the refresh token/session.

Example request:

```json
{
  "refreshToken": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
}
```

---

# Product Endpoints

All Product endpoints use:

```text
/api/v1/products
```

---

## Create Product

```http
POST /api/v1/products
```

Authorization:

```text
ROLE_ADMIN
```

Request:

```json
{
  "productName": "Laptop"
}
```

Response:

```json
{
  "id": 1,
  "productName": "Laptop",
  "createdBy": "onkar",
  "createdOn": "2026-09-02T19:00:00",
  "modifiedBy": null,
  "modifiedOn": null
}
```

---

## Get All Products

```http
GET /api/v1/products
```

Authorization:

```text
ROLE_USER
ROLE_ADMIN
```

Supports pagination and sorting.

Example:

```http
GET /api/v1/products?page=0&size=10
```

Example with sorting:

```http
GET /api/v1/products?page=0&size=10&sort=productName,asc
```

---

## Get Product by ID

```http
GET /api/v1/products/{id}
```

Example:

```http
GET /api/v1/products/1
```

Returns a single Product.

---

## Update Product

```http
PUT /api/v1/products/{id}
```

Authorization:

```text
ROLE_ADMIN
```

Example:

```http
PUT /api/v1/products/1
```

Request:

```json
{
  "productName": "Gaming Laptop"
}
```

---

## Delete Product

```http
DELETE /api/v1/products/{id}
```

Authorization:

```text
ROLE_ADMIN
```

Example:

```http
DELETE /api/v1/products/1
```

Successful response:

```text
204 No Content
```

---

# Item Endpoints

Items are nested resources of Products.

Base URL:

```text
/api/v1/products/{productId}/items
```

---

## Create Item

```http
POST /api/v1/products/{productId}/items
```

Authorization:

```text
ROLE_ADMIN
```

Example:

```http
POST /api/v1/products/1/items
```

Request:

```json
{
  "quantity": 10
}
```

Response:

```json
{
  "id": 1,
  "productId": 1,
  "quantity": 10
}
```

---

## Get Product Items

```http
GET /api/v1/products/{productId}/items
```

Authorization:

```text
ROLE_USER
ROLE_ADMIN
```

Example:

```http
GET /api/v1/products/1/items
```

Pagination:

```http
GET /api/v1/products/1/items?page=0&size=10
```

---

## Get Item

```http
GET /api/v1/products/{productId}/items/{itemId}
```

Example:

```http
GET /api/v1/products/1/items/5
```

The API verifies that Item `5` actually belongs to Product `1`.

---

## Update Item

```http
PUT /api/v1/products/{productId}/items/{itemId}
```

Authorization:

```text
ROLE_ADMIN
```

Example:

```http
PUT /api/v1/products/1/items/5
```

Request:

```json
{
  "quantity": 50
}
```

---

## Delete Item

```http
DELETE /api/v1/products/{productId}/items/{itemId}
```

Authorization:

```text
ROLE_ADMIN
```

Example:

```http
DELETE /api/v1/products/1/items/5
```

Successful response:

```text
204 No Content
```

---

# Complete API Endpoint Reference

Copy this section for quick API reference:

```text
AUTHENTICATION

POST   /api/v1/auth/register
POST   /api/v1/auth/login
POST   /api/v1/auth/refresh
POST   /api/v1/auth/logout


PRODUCTS

POST   /api/v1/products
GET    /api/v1/products
GET    /api/v1/products/{id}
PUT    /api/v1/products/{id}
DELETE /api/v1/products/{id}


ITEMS

POST   /api/v1/products/{productId}/items
GET    /api/v1/products/{productId}/items
GET    /api/v1/products/{productId}/items/{itemId}
PUT    /api/v1/products/{productId}/items/{itemId}
DELETE /api/v1/products/{productId}/items/{itemId}
```

---

# Pagination and Sorting

Collection endpoints support Spring Data pagination.

Basic pagination:

```http
GET /api/v1/products?page=0&size=10
```

Sorting:

```http
GET /api/v1/products?page=0&size=10&sort=productName,asc
```

Descending:

```http
GET /api/v1/products?page=0&size=10&sort=createdOn,desc
```

Items:

```http
GET /api/v1/products/1/items?page=0&size=10&sort=id,asc
```

Pagination response contains information such as:

```json
{
  "content": [],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

---

# Request Validation

The API uses Jakarta Validation.

Example Product validation:

```java
@NotBlank
@Size(min = 2, max = 255)
private String productName;
```

Example Item validation:

```java
@NotNull
@Min(1)
private Integer quantity;
```

Invalid request:

```json
{
  "productName": ""
}
```

returns:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "fieldErrors": [
    {
      "field": "productName",
      "message": "Product name is required"
    }
  ]
}
```

---

# Error Handling

The application uses centralized exception handling through:

```text
@RestControllerAdvice
```

Standard error structure:

```json
{
  "timestamp": "2026-09-02T19:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999",
  "path": "/api/v1/products/999",
  "fieldErrors": null
}
```

Common HTTP status codes:

| Status | Meaning                        |
| ------ | ------------------------------ |
| 200    | Successful request             |
| 201    | Resource created               |
| 204    | Resource deleted               |
| 400    | Invalid request                |
| 401    | Authentication required/failed |
| 403    | Access denied                  |
| 404    | Resource not found             |
| 500    | Internal server error          |

---

# Swagger / OpenAPI

Swagger UI is available at:

```text
http://localhost:8086/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8086/v3/api-docs
```

Swagger allows developers to:

- Explore endpoints.
- View request/response schemas.
- Test APIs.
- Provide JWT authorization.
- Understand available HTTP methods.
- Inspect API parameters.

For protected endpoints, click:

```text
Authorize
```

and provide:

```text
Bearer <ACCESS_TOKEN>
```

---

# Configuration

The application uses:

```text
src/main/resources/application.properties
```

Example local configuration:

```properties
spring.application.name=product-management-api

server.port=8086

spring.datasource.url=jdbc:mysql://localhost:3306/product_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

security.jwt.secret=YOUR_JWT_SECRET
security.jwt.expiration=900000
security.jwt.refresh-expiration=604800000

app.cors.allowed-origins=http://localhost:5173

springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

Never commit real passwords or production JWT secrets to source control.

---

# Local Setup

## Prerequisites

Install:

```text
Java 17+
Maven 3.8+
MySQL 8+
Git
```

Optional:

```text
Docker
Docker Compose
```

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

Verify MySQL:

```bash
mysql --version
```

---

# Create MySQL Database

Login to MySQL:

```bash
mysql -u root -p
```

Create the database:

```sql
CREATE DATABASE product_management;
```

Verify:

```sql
SHOW DATABASES;
```

Select it:

```sql
USE product_management;
```

Spring Boot/Hibernate will create/update the tables when:

```properties
spring.jpa.hibernate.ddl-auto=update
```

is enabled.

---

# Clone the Repository

```bash
git clone https://github.com/starboyonkar/ZestIndiaIT-Product-RESTful-API
```

Move into the project:

```bash
cd product-management-api
```

---

# Configure Database

Update:

```text
src/main/resources/application.properties
```

Set your actual MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/product_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true

spring.datasource.username=root

spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Also configure a strong JWT secret:

```properties
security.jwt.secret=YOUR_LONG_RANDOM_SECRET
```

---

# Running with Maven

Clean and build:

```bash
mvn clean install
```

Run tests:

```bash
mvn test
```

Start the application:

```bash
mvn spring-boot:run
```

The API will be available at:

```text
http://localhost:8086
```

Swagger:

```text
http://localhost:8086/swagger-ui/index.html
```

---

# Running from Spring Tool Suite

1. Import the project as an existing Maven project.
2. Verify Java 17+ is configured.
3. Verify MySQL is running.
4. Configure `application.properties`.
5. Right-click the project.
6. Select:

```text
Run As → Spring Boot App
```

The application should start on:

```text
http://localhost:8086
```

---

# Running with Docker

Build the application:

```bash
docker compose build
```

Start all services:

```bash
docker compose up -d
```

Check running containers:

```bash
docker compose ps
```

View application logs:

```bash
docker compose logs -f app
```

View MySQL logs:

```bash
docker compose logs -f mysql
```

Stop the application:

```bash
docker compose down
```

Stop and remove database volume:

```bash
docker compose down -v
```

The Docker architecture is:

```text
             Docker Compose
                   │
          ┌────────┴────────┐
          │                 │
          ▼                 ▼
   Product API           MySQL
   Port 8080             Port 3306
          │                 │
          └────── JDBC ─────┘
```

---

# Testing

The project uses multiple levels of testing.

## Unit Testing

Tools:

```text
JUnit 5
Mockito
```

Unit tests focus on business logic in service classes.

Example:

```bash
mvn test
```

Important service scenarios:

```text
ProductService
 ├── createProduct()
 ├── getAllProducts()
 ├── getProductById()
 ├── updateProduct()
 └── deleteProduct()

ItemService
 ├── createItem()
 ├── getItemsByProduct()
 ├── getItem()
 ├── updateItem()
 └── deleteItem()
```

---

# Integration Testing

Integration tests use:

```text
@SpringBootTest
H2 Database
```

The test profile uses an in-memory H2 database rather than the development MySQL database.

Test configuration:

```text
src/test/resources/application-test.properties
```

Run all tests:

```bash
mvn clean test
```

---

# Database Indexing

Indexes are added for frequently queried columns.

Product indexes include:

```text
product_name
created_on
```

Item includes:

```text
product_id
```

The Item `product_id` index is particularly useful for:

```sql
SELECT *
FROM item
WHERE product_id = ?;
```

which is executed by the Product → Items API.

---

# CORS

Development frontend origin:

```text
http://localhost:5173
```

Allowed methods:

```text
GET
POST
PUT
DELETE
OPTIONS
```

The allowed origin is configurable through:

```properties
app.cors.allowed-origins=http://localhost:5173
```

For production, replace this with the actual frontend domain.

---

# HTTPS

Production deployments should use HTTPS.

The application supports SSL configuration through environment variables/properties.

Example production configuration:

```properties
server.ssl.enabled=true
server.ssl.key-store=/app/certs/keystore.p12
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=server
```

For production infrastructure, TLS can also be terminated at:

```text
Load Balancer
      ↓
Reverse Proxy
      ↓
Spring Boot Application
```

---

# Environment Variables

Recommended production variables:

```text
SERVER_PORT
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
JWT_SECRET
JWT_EXPIRATION
JWT_REFRESH_EXPIRATION
CORS_ALLOWED_ORIGINS
SSL_ENABLED
SSL_KEYSTORE
SSL_KEYSTORE_PASSWORD
SSL_KEYSTORE_TYPE
SSL_KEY_ALIAS
```

Example:

```text
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/product_management
SPRING_DATASOURCE_USERNAME=product_user
SPRING_DATASOURCE_PASSWORD=<secure-password>

JWT_SECRET=<long-random-secret>

JWT_EXPIRATION=900000
JWT_REFRESH_EXPIRATION=604800000

CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
```

---

# Git and Secret Management

The following files must not contain production secrets:

```text
application.properties
.env
application-prod.properties
```

Never commit:

```text
Database passwords
JWT signing secrets
Private keys
SSL keystores
API keys
Access tokens
Refresh tokens
```

Use environment variables or a dedicated secret-management solution for production.

The project `.gitignore` excludes common local secrets and generated files.

---

# API Request Flow

## Product Creation

```text
POST /api/v1/products
          │
          ▼
JWT Authentication
          │
          ▼
ROLE_ADMIN validation
          │
          ▼
ProductController
          │
          ▼
ProductRequest validation
          │
          ▼
ProductService
          │
          ▼
ProductRepository
          │
          ▼
Hibernate
          │
          ▼
MySQL
```

## Product Retrieval

```text
GET /api/v1/products
          │
          ▼
JWT Authentication
          │
          ▼
ROLE_USER / ROLE_ADMIN
          │
          ▼
ProductController
          │
          ▼
ProductService
          │
          ▼
ProductRepository
          │
          ▼
Page<Product>
          │
          ▼
ProductResponse
          │
          ▼
JSON Response
```

---

# Design Principles

The project follows several REST and software engineering principles.

## Resource-Oriented URLs

Instead of:

```text
/createProduct
/getProducts
/deleteProduct
```

the API uses:

```text
POST   /products
GET    /products
DELETE /products/{id}
```

## HTTP Methods

```text
GET     → Retrieve
POST    → Create
PUT     → Update
DELETE  → Delete
```

## API Versioning

All application APIs use:

```text
/api/v1/
```

This allows future versions such as:

```text
/api/v2/
```

without immediately breaking existing clients.

## DTO Separation

The API does not expose JPA entities directly.

Instead:

```text
Request JSON
     ↓
Request DTO
     ↓
Entity
     ↓
Response DTO
     ↓
Response JSON
```

This keeps the API contract separate from the persistence model.

---

# Future Improvements

Possible future enhancements include:

- Flyway database migrations.
- Redis caching.
- Rate limiting.
- Audit logging.
- Structured JSON logging.
- Correlation IDs.
- Prometheus metrics.
- Grafana monitoring.
- CI/CD using GitHub Actions.
- Kubernetes deployment.
- Nginx reverse proxy.
- Centralized secret management.
- API rate limiting.
- Advanced product search/filtering.
- Soft delete.
- Optimistic locking.
- Database connection pooling optimization.
- Integration with a message broker for asynchronous events.

These components should only be introduced when the application's requirements justify them.

---

# Build and Verification Checklist

Before considering the application ready, verify:

```text
[✓] Java 17+
[✓] Spring Boot
[✓] Spring Web
[✓] Spring Data JPA
[✓] Hibernate
[✓] MySQL
[✓] JWT authentication
[✓] Refresh token
[✓] Refresh token rotation
[✓] Role-based authorization
[✓] Product CRUD
[✓] Item CRUD
[✓] Product → Item relationship
[✓] Jakarta Validation
[✓] Pagination
[✓] Sorting
[✓] Database indexes
[✓] Global exception handling
[✓] Swagger/OpenAPI
[✓] JUnit 5
[✓] Mockito
[✓] Spring Boot integration tests
[✓] H2 test database
[✓] CORS
[✓] HTTPS configuration
[✓] Dockerfile
[✓] Docker Compose
[✓] Git security / .gitignore
```

---

# Quick Start

For a fast local setup:

```bash
# 1. Clone
git clone https://github.com/starboyonkar/ZestIndiaIT-Product-RESTful-API

# 2. Enter project
cd product-management-api

# 3. Create MySQL database
mysql -u root -p
```

```sql
CREATE DATABASE product_management;
```

Configure:

```text
src/main/resources/application.properties
```

Then:

```bash
# 4. Build
mvn clean install

# 5. Run tests
mvn test

# 6. Start application
mvn spring-boot:run
```

Open Swagger:

```text
http://localhost:8086/swagger-ui/index.html
```

---

# Docker Quick Start

```bash
docker compose build
docker compose up -d
docker compose ps
```

API:

```text
http://localhost:8086
```

Swagger:

```text
http://localhost:8086/swagger-ui/index.html
```

Stop:

```bash
docker compose down
```

---

# Author

**Onkar Mahesh Chaugule**

Java | Spring Boot | Backend Development | REST APIs | Full-Stack Development

---

# License

This project is intended for educational, portfolio, and demonstration purposes.
