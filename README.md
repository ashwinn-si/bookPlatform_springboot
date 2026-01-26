# BookPlatform Backend

A RESTful API backend service for managing books, authors, categories, and reviews. Built with **Spring Boot 4.0.1** and **Java 21**, using **Spring Data JPA** with **MySQL** database.

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Database Schema & Relationships](#database-schema--relationships)
- [API Endpoints](#api-endpoints)
- [Getting Started](#getting-started)
- [Architecture](#architecture)

## 🎯 Project Overview

BookPlatform is a comprehensive book management system that allows users to:
- **User Authentication**: Secure signup/login with JWT tokens
- **Role-Based Access**: Admin and User roles with protected endpoints
- Manage authors and their portfolios (Admin only)
- Organize books into multiple categories (Admin only)
- Track book reviews and ratings
- Retrieve paginated lists with search functionality
- Get average star ratings for books

## 🛠 Technology Stack

- **Java 21** - Latest LTS version
- **Spring Boot 4.0.1** - Application framework
- **Spring Data JPA** - ORM and data access
- **Spring Security** - Authentication & authorization
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password hashing
- **MySQL** - Relational database
- **Lombok** - Reduce boilerplate code
- **Jakarta Validation** - Request validation
- **Maven** - Build tool

## 📁 Project Structure

```
src/main/java/com/example/BookPlatform/
├── BookPlatformApplication.java        # Main application entry point
│
├── Config/
│   ├── GlobalErrorHandler.java        # Centralized exception handling
│   ├── SecurityConfig.java            # Spring Security & CORS configuration
│   ├── JwtHandler.java                # JWT token validation filter
│   └── IsAdminHandler.java            # Admin authorization filter
│
├── Controller/                         # REST API endpoints
│   ├── AuthController.java
│   ├── AuthorController.java
│   ├── BookController.java
│   ├── CategoryController.java
│   └── ReviewController.java
│
├── Domain/                             # JPA Entity models
│   ├── Author.java
│   ├── Book.java
│   ├── Category.java
│   ├── Review.java
│   └── User.java
│
├── DTO/                                # Data Transfer Objects
│   ├── GetAllDTO.java
│   └── JwtDTO.java
│
├── Repository/                         # Spring Data JPA repositories
│   ├── AuthorRepository.java
│   ├── BookRepository.java
│   ├── CategoryRepository.java
│   ├── ReviewRepository.java
│   └── UserRepository.java
│
├── Service/                            # Business logic layer
│   ├── AuthService.java
│   ├── AuthorService.java
│   ├── BookService.java
│   ├── CategoryService.java
│   └── ReviewService.java
│
└── Utils/                              # Utility classes
    ├── BcryptUtil.java                # Password hashing utility
    ├── CustomError.java               # Custom exception class
    ├── JwtUtil.java                   # JWT token generation/validation
    └── ResponseHandler.java           # Standardized API response wrapper
```

## 🗄 Database Schema & Relationships

### Entity Relationships

```
User (Independent)
  ↓
  email, password, role (ADMIN/USER)

Author (1) ──────< (N) Book (N) >────── (N) Category
                         |
                         |
                         | (1)
                         |
                         ∨
                       (N) Review
```

### Entities

#### **User**
- **Table**: `users`
- **Relationships**: None (independent)
- **Fields**:
  - `id` (Primary Key)
  - `email` (Unique, not null)
  - `password` (BCrypt hashed)
  - `role` (Enum: ADMIN, USER)

#### **Author**
- **Table**: `authors`
- **Relationships**: One-to-Many with Book
- **Fields**:
  - `id` (Primary Key)
  - `name`

#### **Book**
- **Table**: `books`
- **Relationships**:
  - Many-to-One with Author
  - Many-to-Many with Category (via `book_category` join table)
  - One-to-Many with Review
- **Fields**:
  - `id` (Primary Key)
  - `name`
  - `author_id` (Foreign Key)

#### **Category**
- **Table**: `categories`
- **Relationships**: Many-to-Many with Book
- **Fields**:
  - `id` (Primary Key)
  - `name`

#### **Review**
- **Table**: `reviews`
- **Relationships**: Many-to-One with Book
- **Fields**:
  - `id` (Primary Key)
  - `message`
  - `stars` (0-5)
  - `book_id` (Foreign Key)

## 📡 API Endpoints

### 🔒 Authentication & Authorization

**Authentication**: JWT tokens required for protected endpoints  
**Authorization**: Admin role required for all create/update/delete operations

#### How to Authenticate:
1. **Signup**: `POST /api/auth/signup`
2. **Login**: `POST /api/auth/login` (returns JWT token)
3. **Use Token**: Include in `Authorization` header as `Bearer <token>`

#### Protected Endpoints:
- **Admin-only routes**: `/api/*/admin/*` (requires `ADMIN` role)
- **User routes**: `/api/*/protected/*` (requires any authenticated user)

---

### **Authentication Endpoints** (`/api/auth`)

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/signup` | Register new user | `{ "email": "user@example.com", "password": "string" }` | Success message |
| POST | `/login` | Login user | `{ "email": "user@example.com", "password": "string" }` | `{ "userId": number, "role": "USER/ADMIN", "token": "jwt_token" }` |

**Notes**:
- Default role: `USER`
- Passwords are hashed using BCrypt
- JWT tokens are required for protected/admin endpoints

---

### **Author Endpoints** (`/api/author`)

| Method | Endpoint | Description | Auth Required | Request Body | Query Params |
|--------|----------|-------------|---------------|--------------|--------------|
| GET | `/get-all-author` | Get all authors (paginated) | No | - | `page` (required, min: 1)<br>`size` (required, min: 1)<br>`name` (optional) |
| GET | `/get-author/{authorId}` | Get author by ID | No | - | - |
| POST | `/admin/add-author` | Create new author | **Admin** | `{ "name": "string" }` | - |
| PUT | `/admin/update-author/{authorId}` | Update author | **Admin** | `{ "name": "string" }` | - |
| DELETE | `/admin/delete-author/{authorId}` | Delete author and related books | **Admin** | - | - |

### **Book Endpoints** (`/api/book`)

| Method | Endpoint | Description | Auth Required | Request Body | Query Params |
|--------|----------|-------------|---------------|--------------|--------------|
| GET | `/get-all-book` | Get all books (paginated) | No | - | `page` (required, min: 1)<br>`size` (required, min: 1)<br>`name` (optional) |
| GET | `/get-book/{bookId}` | Get book by ID with avg rating | No | - | - |
| POST | `/admin/add-book` | Create new book | **Admin** | `{ "name": "string", "authorId": number, "categories": [number] }` | - |
| PUT | `/admin/update-book/{bookId}` | Update book details | **Admin** | `{ "name": "string" (optional), "authorId": number (optional), "categories": [number] (optional) }` | - |
| DELETE | `/admin/delete-book/{bookId}` | Delete book | **Admin** | - | - |

### **Category Endpoints** (`/api/category`)

| Method | Endpoint | Description | Auth Required | Request Body | Query Params |
|--------|----------|-------------|---------------|--------------|--------------|
| GET | `/get-all-category` | Get all categories (paginated) | No | - | `page` (required, min: 1)<br>`size` (required, min: 1)<br>`name` (optional) |
| GET | `/get-category/{categoryId}` | Get category by ID | No | - | - |
| POST | `/admin/add-category` | Create new category | **Admin** | `{ "name": "string" }` | - |
| PUT | `/admin/update-category/{categoryId}` | Update category | **Admin** | `{ "name": "string" }` | - |
| DELETE | `/admin/delete-category/{categoryId}` | Delete category | **Admin** | - | - |

### **Review Endpoints** (`/api/review`)

| Method | Endpoint | Description | Auth Required | Request Body | Query Params |
|--------|----------|-------------|---------------|--------------|--------------|
| GET | `/get-all-review/{bookId}` | Get all reviews for a book | No | - | `page` (required)<br>`size` (required) |
| POST | `/add-review/{bookId}` | Add review to a book | No | `{ "message": "string", "stars": number (0-5) }` | - |
| PUT | `/update-review/{reviewId}` | Update review | No | `{ "message": "string" (optional), "stars": number (optional, 0-5) }` | - |
| DELETE | `/delete-review/{bookId}` | Delete review | No | - | - |

### Response Format

All API responses follow a standardized format using `ResponseHandler`:

```json
{
  "statusCode": 200,
  "data": { /* response data */ },
  "message": "success message"
}
```

## 🚀 Getting Started

### Prerequisites

- Java 21 (JDK)
- Maven 3.6+
- MySQL 8.0+

### Database Configuration

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/book_platform
spring.datasource.username=root
spring.datasource.password=root
```

### Build & Run

#### Using Maven:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

#### Using VS Code Task:
Run the "Run Java" task which executes `~/coding/java_run.sh`

### Access the API

The application runs on `http://localhost:8080`

#### Public Endpoint Example:
```bash
curl http://localhost:8080/api/book/get-all-book?page=1&size=10
```

#### Authentication Example:
```bash
# 1. Login to get JWT token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"password"}'

# Response: {"statusCode":200,"data":{"userId":1,"role":"ADMIN","token":"eyJhbGc..."},"message":"login successful"}

# 2. Use token for admin endpoints
curl -X POST http://localhost:8080/api/book/admin/add-book \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGc..." \
  -d '{"name":"New Book","authorId":1,"categories":[1,2]}'
```

## 🏗 Architecture

### Layered Architecture Pattern

The application follows a clean three-tier architecture:

```
Controller Layer (REST API)
        ↓
Service Layer (Business Logic)
        ↓
Repository Layer (Data Access)
        ↓
Database (MySQL)
```

### Key Design Principles

1. **Separation of Concerns**: Each layer has a distinct responsibility
2. **Security First**: JWT-based authentication with role-based authorization
3. **Dependency Injection**: Constructor-based injection for loose coupling
4. **DTO Pattern**: DTOs defined inline or separately for request/response mapping
5. **Centralized Error Handling**: `GlobalErrorHandler` manages all exceptions
6. **Validation**: Jakarta Validation annotations on DTOs and method parameters
7. **Transactional Management**: `@Transactional` for data consistency
8. **Standardized Responses**: `ResponseHandler` wraps all API responses
9. **Password Security**: BCrypt hashing for all user passwords
10. **CORS Configuration**: Pre-configured for local frontend development (ports 3000, 5173)

### Error Handling

Custom exceptions are handled globally:
- `CustomError` - Business logic exceptions with HTTP status codes
- `MethodArgumentNotValidException` - Validation errors
- `ConstraintViolationException` - Database constraint violations
- Generic `Exception` - Unexpected errors (500 Internal Server Error)

### Data Validation

- Request DTOs use Jakarta Validation (`@NotNull`, `@NotBlank`, `@Min`, `@Max`, `@Size`)
- Path variables and query parameters validated using `@Validated` on controllers
- Service layer performs additional business logic validation

## 📝 Notes

### General
- **Pagination**: All list endpoints use 1-based page numbers (internally converted to 0-based for Spring Data)
- **Cascade Operations**: Deleting an author cascades to their books; deleting books cascades to reviews
- **Lazy Loading**: Most relationships use `FetchType.LAZY` for performance
- **Average Ratings**: Books automatically calculate average star ratings from reviews
- **Search**: Name-based search available on authors, books, and categories

### Security
- **JWT Tokens**: Stateless authentication with no server-side session storage
- **Token Format**: Include as `Authorization: Bearer <token>` header
- **Password Hashing**: BCrypt with automatic salt generation
- **Default Role**: New users assigned `USER` role (cannot perform admin operations)
- **Admin Access**: Only users with `ADMIN` role can create/update/delete authors, books, and categories
- **Public Access**: All GET endpoints (read operations) are publicly accessible
- **CORS**: Configured for `localhost:3000` and `localhost:5173` (React/Vite development servers)

---

**Version**: 0.0.1-SNAPSHOT  
**License**: To be determined  
**Built with**: Spring Boot 4.0.1, Java 21
