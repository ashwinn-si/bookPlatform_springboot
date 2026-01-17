# BookPlatform Backend - AI Coding Instructions

## Project Overview
A Spring Boot 4.0 (Java 21) REST API for managing books, authors, categories, and reviews. Uses Spring Data JPA with MySQL.

## Architectural Patterns
- **Layered Architecture**: `Controller` -> `Service` -> `Repository`.
- **API Responses**: Always wrap responses using `ResponseHandler.handleResponse(HttpStatus, data, message)`.
- **Error Handling**: Use `CustomError` for business exceptions. Centralized handling in `GlobalErrorHandler`.
- **Domain Modeling**:
  - Use Lombok `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`.
  - JPA Entities in `com.example.BookPlatform.Domain`.
  - Explicit `@Table` names (e.g., `books`, `authors`).

## Code Conventions

### Services
- Handle existence checks within services using private helper methods:
  ```java
  private Book isBookExsits(Integer bookId) {
      return bookRepository.findById(bookId).orElseThrow(() -> new CustomError(HttpStatus.NOT_FOUND, "book not found"));
  }
  ```
- Use `@Transactional` for state-changing operations.
- Inject dependencies via constructor injection.

### Controllers
- Use `@RestController` and `@Validated`.
- Define request DTOs with validation annotations (`@NotNull`, `@Size`, `@Min`).
- DTOs can be defined in the same file as the controller or in `com.example.BookPlatform.DTO`.

### Pagination
- Use `GetAllDTO<T>` for paginated responses.
- Note: `PageRequest.of(page - 1, size)` is standard as the API expects 1-based page numbers.

## Critical Workflows
- **Build**: `./mvnw clean install`
- **Run**: Use the "Run Java" VS Code task (`~/coding/java_run.sh`).
- **Database**: Connects to `jdbc:mysql://127.0.0.1:3306/book_platform` (credentials: root/root).

## Key Files
- [BookService.java](../src/main/java/com/example/BookPlatform/Service/BookService.java): Exemplifies service pattern and DTO usage.
- [BookController.java](../src/main/java/com/example/BookPlatform/Controller/BookController.java): Shows response wrapping and validation logic.
- [GlobalErrorHandler.java](../src/main/java/com/example/BookPlatform/Config/GlobalErrorHandler.java): Defines exception mappings.
