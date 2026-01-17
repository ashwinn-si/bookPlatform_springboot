package com.example.BookPlatform.Service;

import com.example.BookPlatform.DTO.GetAllDTO;
import com.example.BookPlatform.Domain.Book;
import com.example.BookPlatform.Domain.Review;
import com.example.BookPlatform.Repository.BookRepository;
import com.example.BookPlatform.Repository.ReviewRepository;
import com.example.BookPlatform.Utils.CustomError;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ReviewDTO {
  // future la users will come
  Integer reviewId;
  String message;
  Integer stars;
}

@Service
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final BookRepository bookRepository;

  ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
    this.reviewRepository = reviewRepository;
    this.bookRepository = bookRepository;
  }

  public GetAllDTO<ReviewDTO> getAllBookReviews(Integer page, Integer size, Integer bookId) {
    PageRequest pageRequest = PageRequest.of(page - 1, size);
    Page<Review> content = reviewRepository.findByBookId(pageRequest, bookId);
    List<ReviewDTO> reviewList = new ArrayList<>();
    for (Review review : content.getContent()) {
      reviewList.add(new ReviewDTO(review.getId(), review.getMessage(), review.getStars()));
    }
    return new GetAllDTO<>(content.getTotalPages(), page, size, reviewList);
  }

  @Transactional
  public void addReview(String message, Integer stars, Integer bookId) {
    Book book = isBookExists(bookId);
    Review review = new Review(message, stars);
    review.setBook(book);
    reviewRepository.save(review);
  }

  @Transactional
  public void updateReview(String message, Integer stars, Integer reviewId) {
    Review review = isReviewExists(reviewId);
    if (message != null) {
      review.setMessage(message);
    }
    if (stars != null) {
      review.setStars(stars);
    }
    reviewRepository.save(review);
  }

  @Transactional
  public void deleteReview(Integer reviewId) {
    isReviewExists(reviewId);
    reviewRepository.deleteById(reviewId);
  }

  private Book isBookExists(Integer bookId) {
    return bookRepository.findById(bookId).orElseThrow(() -> new CustomError(HttpStatus.NOT_FOUND, "book not found"));
  }

  private Review isReviewExists(Integer reviewId) {
    return reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomError(HttpStatus.NOT_FOUND, "review not found"));
  }
}
