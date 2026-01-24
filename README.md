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
- Manage authors and their portfolios
- Organize books into multiple categories
- Track book reviews and ratings
- Retrieve paginated lists with search functionality
- Get average star ratings for books

## 🛠 Technology Stack

- **Java 21** - Latest LTS version
- **Spring Boot 4.0.1** - Application framework
- **Spring Data JPA** - ORM and data access
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
│   └── GlobalErrorHandler.java        # Centralized exception handling
│
├── Controller/                         # REST API endpoints
│   ├── AuthorController.java
│   ├── BookController.java
│   ├── CategoryController.java
│   └── ReviewController.java
│
├── Domain/                             # JPA Entity models
│   ├── Author.java
│   ├── Book.java
│   ├── Category.java
│   └── Review.java
│
├── DTO/                                # Data Transfer Objects
│   └── GetAllDTO.java
│
├── Repository/                         # Spring Data JPA repositories
│   ├── AuthorRepository.java
│   ├── BookRepository.java
│   ├── CategoryRepository.java
│   └── ReviewRepository.java
│
├── Service/                            # Business logic layer
│   ├── AuthorService.java
│   ├── BookService.java
│   ├── CategoryService.java
│   └── ReviewService.java
│
└── Utils/                              # Utility classes
    ├── CustomError.java               # Custom exception class
    └── ResponseHandler.java           # Standardized API response wrapper
```

## 🗄 Database Schema & Relationships

### Entity Relationships

```
Author (1) ──────< (N) Book (N) >────── (N) Category
                         |
                         |
                         | (1)
                         |
                         ∨
                       (N) Review
```

### Entities

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

### **Author Endpoints** (`/api/author`)

| Method | Endpoint | Description | Request Body | Query Params |
|--------|----------|-------------|--------------|--------------|
| GET | `/get-all-author` | Get all authors (paginated) | - | `page` (required, min: 1)<br>`size` (required, min: 1)<br>`name` (optional) |
| GET | `/get-author/{authorId}` | Get author by ID | - | - |
| POST | `/add-author` | Create new author | `{ "name": "string" }` | - |
| PUT | `/update-author/{authorId}` | Update author | `{ "name": "string" }` | - |
| DELETE | `/delete-author/{authorId}` | Delete author and related books | - | - |

### **Book Endpoints** (`/api/book`)

| Method | Endpoint | Description | Request Body | Query Params |
|--------|----------|-------------|--------------|--------------|
| GET | `/get-all-book` | Get all books (paginated) | - | `page` (required, min: 1)<br>`size` (required, min: 1)<br>`name` (optional) |
| GET | `/get-book/{bookId}` | Get book by ID with avg rating | - | - |
| POST | `/add-book` | Create new book | `{ "name": "string", "authorId": number, "categories": [number] }` | - |
| PUT | `/update-book/{bookId}` | Update book details | `{ "name": "string" (optional), "authorId": number (optional), "categories": [number] (optional) }` | - |
| DELETE | `/delete-book/{bookId}` | Delete book | - | - |

### **Category Endpoints** (`/api/category`)

| Method | Endpoint | Description | Request Body | Query Params |
|--------|----------|-------------|--------------|--------------|
| GET | `/get-all-category` | Get all categories (paginated) | - | `page` (required, min: 1)<br>`size` (required, min: 1)<br>`name` (optional) |
| GET | `/get-category/{categoryId}` | Get category by ID | - | - |
| POST | `/add-category` | Create new category | `{ "name": "string" }` | - |
| PUT | `/update-category/{categoryId}` | Update category | `{ "name": "string" }` | - |
| DELETE | `/delete-category/{categoryId}` | Delete category | - | - |

### **Review Endpoints** (`/api/review`)

| Method | Endpoint | Description | Request Body | Query Params |
|--------|----------|-------------|--------------|--------------|
| GET | `/get-all-review/{bookId}` | Get all reviews for a book | - | `page` (required)<br>`size` (required) |
| POST | `/add-review/{bookId}` | Add review to a book | `{ "message": "string", "stars": number (0-5) }` | - |
| PUT | `/update-review/{reviewId}` | Update review | `{ "message": "string" (optional), "stars": number (optional, 0-5) }` | - |
| DELETE | `/delete-review/{bookId}` | Delete review | - | - |

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

Example request:
```bash
curl http://localhost:8080/api/book/get-all-book?page=1&size=10
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
2. **Dependency Injection**: Constructor-based injection for loose coupling
3. **DTO Pattern**: DTOs defined inline or separately for request/response mapping
4. **Centralized Error Handling**: `GlobalErrorHandler` manages all exceptions
5. **Validation**: Jakarta Validation annotations on DTOs and method parameters
6. **Transactional Management**: `@Transactional` for data consistency
7. **Standardized Responses**: `ResponseHandler` wraps all API responses

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

- **Pagination**: All list endpoints use 1-based page numbers (internally converted to 0-based for Spring Data)
- **Cascade Operations**: Deleting an author cascades to their books; deleting books cascades to reviews
- **Lazy Loading**: Most relationships use `FetchType.LAZY` for performance
- **Average Ratings**: Books automatically calculate average star ratings from reviews
- **Search**: Name-based search available on authors, books, and categories

## 📚 API Usage Examples

### Creating a Complete Book Entry

```bash
# 1. Create an author
curl -X POST http://localhost:8080/api/author/add-author \
  -H "Content-Type: application/json" \
  -d '{"name": "J.K. Rowling"}'

# 2. Create categories
curl -X POST http://localhost:8080/api/category/add-category \
  -H "Content-Type: application/json" \
  -d '{"name": "Fantasy"}'

curl -X POST http://localhost:8080/api/category/add-category \
  -H "Content-Type: application/json" \
  -d '{"name": "Young Adult"}'

# 3. Create a book with author and categories
curl -X POST http://localhost:8080/api/book/add-book \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Harry Potter and the Philosophers Stone",
    "authorId": 1,
    "categories": [1, 2]
  }'

# 4. Add a review
curl -X POST http://localhost:8080/api/review/add-review/1 \
  -H "Content-Type: application/json" \
  -d '{
    "message": "A magical journey!",
    "stars": 5
  }'
```

### Searching and Filtering

```bash
# Search books by name
curl "http://localhost:8080/api/book/get-all-book?page=1&size=10&name=Harry"

# Get paginated authors
curl "http://localhost:8080/api/author/get-all-author?page=1&size=5"

# Get book details with average rating
curl "http://localhost:8080/api/book/get-book/1"
```

## 🛠 Development Guidelines

### Adding New Features

When implementing new features, follow these patterns:

1. **Service Layer**: Create private helper methods for existence checks
   ```java
   private Book isBookExists(Integer bookId) {
       return bookRepository.findById(bookId)
           .orElseThrow(() -> new CustomError(HttpStatus.NOT_FOUND, "book not found"));
   }
   ```

2. **Controller DTOs**: Define request DTOs with validation annotations
   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   class RequestDTO {
       @NotNull(message = "field is required")
       @Size(min = 1, message = "field cannot be empty")
       private String field;
   }
   ```

3. **Response Wrapping**: Always use `ResponseHandler.handleResponse()`
   ```java
   return ResponseHandler.handleResponse(
       HttpStatus.OK, 
       data, 
       "success message"
   );
   ```

4. **Transaction Management**: Use `@Transactional` for state-changing operations
   ```java
   @Transactional
   public void updateResource(Integer id, String newValue) {
       // Your update logic
   }
   ```

### Code Conventions

- Use **Lombok** annotations: `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Data`
- Use **constructor injection** for dependencies
- Explicit `@Table` names for entities (e.g., `books`, `authors`)
- Keep DTOs close to controllers or in the `DTO` package
- Private helper methods for validation in services

## 🐛 Troubleshooting

### Common Issues

**Database Connection Failed**
```
Error: Communications link failure
```
- Ensure MySQL is running: `mysql.server start` (macOS)
- Verify database exists: `CREATE DATABASE book_platform;`
- Check credentials in `application.properties`

**Port Already in Use**
```
Error: Port 8080 is already in use
```
- Change port in `application.properties`: `server.port=8081`
- Or kill existing process: `lsof -ti:8080 | xargs kill -9`

**Build Failures**
```
Error: JAVA_HOME not set
```
- Set JAVA_HOME: `export JAVA_HOME=$(/usr/libexec/java_home -v 21)`
- Verify Java version: `java -version` (should be 21)

**Validation Errors**
- Check request body matches DTO structure
- Ensure required fields are not null
- Verify numeric constraints (min/max values)

### Database Schema Creation

The application uses JPA auto-creation. To manually create tables:

```sql
CREATE DATABASE book_platform;
USE book_platform;

-- Tables will be auto-generated by Hibernate on first run
-- Or set in application.properties:
-- spring.jpa.hibernate.ddl-auto=create (or update)
```

## 🧪 Testing

Run tests with Maven:
```bash
./mvnw test
```

For specific test classes:
```bash
./mvnw test -Dtest=BookPlatformApplicationTests
```

## 🔐 Security Considerations

- **CORS**: Configured in [CorsConfig.java](src/main/java/com/example/BookPlatform/Config/CorsConfig.java)
- **Input Validation**: Jakarta Validation prevents malformed requests
- **SQL Injection**: Prevented by Spring Data JPA parameterized queries
- **Error Messages**: Generic messages in production to avoid information leakage

## 📈 Future Enhancements

- [ ] User authentication and authorization (Spring Security)
- [ ] Pagination metadata in responses
- [ ] Book cover image uploads
- [ ] Advanced search with multiple filters
- [ ] Caching layer (Redis)
- [ ] API documentation (Swagger/OpenAPI)
- [ ] Docker containerization
- [ ] CI/CD pipeline

## 🤝 Contributing

1. Follow the existing code structure and patterns
2. Use the provided code conventions
3. Ensure all tests pass before committing
4. Update README for significant changes

---

**Version**: 0.0.1-SNAPSHOT  
**Author**: Ashwin  
**License**: To be determined  
**Built with**: Spring Boot 4.0.1, Java 21, MySQL 8.0
