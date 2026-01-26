package com.example.BookPlatform.Controller;

import com.example.BookPlatform.DTO.JwtDTO;
import com.example.BookPlatform.Service.ReviewService;
import com.example.BookPlatform.Utils.ResponseHandler;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
class AddReviewDTO {
    @Size(min = 0, message = "message is review")
    private String message;
    @Min(value = 0, message = "stars cant be negative")
    @Max(value = 5, message = "stars must be lesser than 5")
    private Integer stars;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class UpdateReviewDTO {
    @Nullable
    private String message;
    @Nullable
    @Min(value = 0, message = "stars cant be negative")
    @Max(value = 5, message = "stars must be between 0 and 5")
    private Integer stars;
}

@RestController
@RequestMapping("/api/review")
@Validated
public class ReviewController {

  private final ReviewService reviewService;

  ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping("/protected/add-review/{bookId}")
  ResponseEntity<?> addReview(@AuthenticationPrincipal JwtDTO jwtDTO, @PathVariable @NotNull(message = "book id is required") Integer bookId,
      @RequestBody @Valid AddReviewDTO addReviewDTO) {
    reviewService.addReview(jwtDTO.getUserId(), addReviewDTO.getMessage(), addReviewDTO.getStars(), bookId);
    return ResponseHandler.handleResponse(HttpStatus.CREATED, null, "book review added");
  }

  @GetMapping("/get-all-review/{bookId}")
  ResponseEntity<?> getAllReview(@PathVariable @NotNull(message = "book id is required") Integer bookId,
      @RequestParam @NotNull(message = "page is required") Integer page,
      @RequestParam @NotNull(message = "size is required") Integer size) {
    return ResponseHandler.handleResponse(HttpStatus.CREATED, reviewService.getAllBookReviews(page, size, bookId),
        "all book reviews");
  }

  @DeleteMapping("/protected/delete-review/{reviewId}")
  ResponseEntity<?> deleteReview(@AuthenticationPrincipal JwtDTO jwtDTO, @PathVariable @NotNull(message = "reviewId id is required") Integer reviewId) {
    reviewService.deleteReview(jwtDTO.getUserId(), reviewId);
    return ResponseHandler.handleResponse(HttpStatus.CREATED, null, "book review updated");
  }

  @PutMapping("/protected/update-review/{reviewId}")
  ResponseEntity<?> updateReview(@AuthenticationPrincipal JwtDTO jwtDTO, @PathVariable @NotNull(message = "review id is required") Integer reviewId,
                                 @RequestBody @Valid UpdateReviewDTO updateReviewDTO) {
    reviewService.updateReview(jwtDTO.getUserId(), updateReviewDTO.getMessage(), updateReviewDTO.getStars(), reviewId);
    return ResponseHandler.handleResponse(HttpStatus.CREATED, null, "book review deleted");
  }
}
