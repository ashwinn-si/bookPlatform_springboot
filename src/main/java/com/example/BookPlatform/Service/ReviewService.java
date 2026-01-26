package com.example.BookPlatform.Service;

import com.example.BookPlatform.DTO.GetAllDTO;
import com.example.BookPlatform.Domain.Book;
import com.example.BookPlatform.Domain.Review;
import com.example.BookPlatform.Domain.User;
import com.example.BookPlatform.Repository.BookRepository;
import com.example.BookPlatform.Repository.ReviewRepository;
import com.example.BookPlatform.Repository.UserRepository;
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
  private final UserRepository userRepository;

  ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository, UserRepository userRepository) {
    this.reviewRepository = reviewRepository;
    this.bookRepository = bookRepository;
    this.userRepository = userRepository;
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
  public void addReview(Long userId, String message, Integer stars, Integer bookId) {
    User user = isUserExists(userId);
    Book book = isBookExists(bookId);
    Review review = new Review(message, stars);
    review.setUser(user);
    review.setBook(book);
    reviewRepository.save(review);
  }

  @Transactional
  public void updateReview(Long userId, String message, Integer stars, Integer reviewId) {
    Review review = isReviewBelongsToUser(userId, reviewId);
    if (message != null) {
      review.setMessage(message);
    }
    if (stars != null) {
      review.setStars(stars);
    }
    reviewRepository.save(review);
  }

  @Transactional
  public void deleteReview(Long userId, Integer reviewId) {
    isReviewBelongsToUser(userId, reviewId);
    reviewRepository.deleteById(reviewId);
  }

  private Book isBookExists(Integer bookId) {
    return bookRepository.findById(bookId).orElseThrow(() -> new CustomError(HttpStatus.NOT_FOUND, "book not found"));
  }

  private Review isReviewExists(Integer reviewId) {
    return reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomError(HttpStatus.NOT_FOUND, "review not found"));
  }

  private User isUserExists(Long userId){
      return userRepository.findById(userId)
              .orElseThrow(() -> new CustomError(HttpStatus.NOT_FOUND, "user not found"));
  }

  private Review isReviewBelongsToUser(Long userId, Integer reviewId){
    Review review = isReviewExists(reviewId);
    User user = isUserExists(userId);

    if(review.getUser().getId() != userId){
        throw new CustomError(HttpStatus.CONFLICT, "user is trying to do something wrong");
    }
    return review;
  }
}
