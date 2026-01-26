package com.example.BookPlatform.Controller;

import com.example.BookPlatform.Service.AuthorService;
import com.example.BookPlatform.Utils.ResponseHandler;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
class AuthorDTO {
    @NotBlank(message = "name is required")
    @Size(min = 1, message = "name should be of atleast size 1")
    private String name;

}

@RestController
@RequestMapping("/api/author")
@Validated
public class AuthorController {

  private final AuthorService authorService;

  AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @GetMapping("/get-all-author")
  ResponseEntity<?> getAllAuthor(
          @RequestParam @Min(value = 1, message = "page is required") Integer page,
          @RequestParam @Min(value = 1, message = "size is required") Integer size,
          @RequestParam @Nullable String name) {
    return ResponseHandler.handleResponse(HttpStatus.OK, authorService.getAllAuthor(page, size, name),
        "all authors details");
  }

  @GetMapping("/get-author/{authorId}")
  ResponseEntity<?> getAuthor(@PathVariable @Min(value = 0, message = "id is required") Integer authorId,
                              @RequestParam @Min(value = 1, message = "page is required") Integer page,
                              @RequestParam @Min(value = 1, message = "size is required") Integer size) {
    return ResponseHandler.handleResponse(HttpStatus.OK, authorService.getAuthor(authorId, page, size), "author details");
  }

  @PostMapping("/admin/add-author")
  ResponseEntity<?> addAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
    authorService.addAuthor(authorDTO.getName());
    return ResponseHandler.handleResponse(HttpStatus.CREATED, null, "author created");
  }

  @PutMapping("/admin/update-author/{authorId}")
  ResponseEntity<?> updateAuthor(@PathVariable @Min(value = 0, message = "id is required") Integer authorId,
      @RequestBody @Valid AuthorDTO authorDTO) {
    authorService.updateAuthor(authorId, authorDTO.getName());
    return ResponseHandler.handleResponse(HttpStatus.OK, null, "author details updated");
  }

  @DeleteMapping("/admin/delete-author/{authorId}")
  ResponseEntity<?> deleteAuthor(@PathVariable @Min(value = 0, message = "id is required") Integer authorId) {
    authorService.deleteAuthor(authorId);
    return ResponseHandler.handleResponse(HttpStatus.OK, null, "author and the related books deleted");
  }
}
