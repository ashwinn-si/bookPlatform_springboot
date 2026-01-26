package com.example.BookPlatform.Controller;

import com.example.BookPlatform.Service.CategoryService;
import com.example.BookPlatform.Utils.ResponseHandler;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
class CategoryRequestDTO {
    @NotBlank(message = "name is required")
    @Size(min = 1, message = "name should be of at least size 1")
    private String name;
}

@RestController
@RequestMapping("/api/category")
@Validated
public class CategoryController {


    private final CategoryService categoryService;

  CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/get-all-category")
  public ResponseEntity<?> getAllCategory(
      @RequestParam @NotNull(message = "page is required") @Min(1) Integer page,
      @RequestParam @NotNull(message = "size is required") @Min(1) Integer size,
      @RequestParam @Nullable String name) {
    return ResponseHandler.handleResponse(HttpStatus.OK, categoryService.getAllCategory(page, size, name),
        "all categories details");
  }

  @GetMapping("/get-category/{categoryId}")
  public ResponseEntity<?> getCategory(
      @PathVariable @NotNull(message = "categoryId is required") @Min(0) Integer categoryId) {
    return ResponseHandler.handleResponse(HttpStatus.OK, categoryService.getCategory(categoryId), "category details");
  }

  @PostMapping("/admin/add-category")
  public ResponseEntity<?> addCategory(@RequestBody @Valid CategoryRequestDTO categoryDTO) {
    categoryService.addCategory(categoryDTO.getName());
    return ResponseHandler.handleResponse(HttpStatus.CREATED, null, "category created");
  }

  @PutMapping("/admin/update-category/{categoryId}")
  public ResponseEntity<?> updateCategory(
      @PathVariable @NotNull(message = "categoryId is required") @Min(0) Integer categoryId,
      @RequestBody @Valid CategoryRequestDTO categoryDTO) {
    categoryService.updateCategory(categoryId, categoryDTO.getName());
    return ResponseHandler.handleResponse(HttpStatus.OK, null, "category details updated");
  }

  @DeleteMapping("/admin/delete-category/{categoryId}")
  public ResponseEntity<?> deleteCategory(
      @PathVariable @NotNull(message = "categoryId is required") @Min(0) Integer categoryId) {
    categoryService.deleteCategory(categoryId);
    return ResponseHandler.handleResponse(HttpStatus.OK, null, "category and the related books deleted");
  }
}
