package com.example.BookPlatform.Controller;

import com.example.BookPlatform.Service.BookService;
import com.example.BookPlatform.Utils.ResponseHandler;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class AddBookDTO{
    @NotNull(message = "name can be empty")
    String name;
    @NotNull(message = "author id is required")
    Integer authorId;
    @NotNull(message = "categories is required")
    List<Integer> categories;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class UpdateBookDTO{
    @Nullable
    String name;
    @Nullable
    Integer authorId;
    @Nullable
    List<Integer> categories;
}

@RestController
@Validated
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;

    BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/get-all-book")
    ResponseEntity<?> getAllBook(@RequestParam @NotNull(message = "page is required") @Min(1) Integer page,
                                 @RequestParam @NotNull(message = "size is required") @Min(1) Integer size,
                                 @RequestParam @Nullable  @Size(min = 1) String name){
        return ResponseHandler.handleResponse(HttpStatus.OK, bookService.getAllBook(page, size, name), "all books details");
    }

    @GetMapping("/get-book/{bookId}")
    ResponseEntity<?> getBook(@PathVariable @NotNull(message = "bookId is required") @Min(0) Integer bookId){
        return ResponseHandler.handleResponse(HttpStatus.OK, bookService.getBook(bookId), "book details");
    }

    @PostMapping("/admin/add-book")
    ResponseEntity<?> addBook(@RequestBody @Valid AddBookDTO addBookDTO){
        bookService.addBook(addBookDTO.getName(), addBookDTO.getAuthorId(), addBookDTO.getCategories());
        return ResponseHandler.handleResponse(HttpStatus.CREATED, null, "book added");
    }

    @PutMapping("/admin/update-book/{bookId}")
    ResponseEntity<?> updateBook(@PathVariable @NotNull(message = "bookId is required") @Min(0) Integer bookId,
                                 @RequestBody @Valid UpdateBookDTO updateBookDTO){
        bookService.updateBook(bookId, updateBookDTO.getAuthorId(), updateBookDTO.getName(), updateBookDTO.getCategories());
        return ResponseHandler.handleResponse(HttpStatus.OK, null, "updated book added");
    }

    @DeleteMapping("/admin/delete-book/{bookId}")
    ResponseEntity<?> deleteBook(@PathVariable @NotNull(message = "bookId is required") @Min(0) Integer bookId){
        bookService.deleteBook(bookId);
        return ResponseHandler.handleResponse(HttpStatus.OK, null, "book deleted");
    }
}
