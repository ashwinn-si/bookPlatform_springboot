package com.example.BookPlatform.Repository;

import com.example.BookPlatform.Domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
  public Page<Book> findByNameContaining(PageRequest pageRequest, String name);

  @Query(value = "SELECT ROUND(COALESCE(AVG(reviews.stars), 0), 2) " +
      "FROM reviews WHERE book_id = ?1", nativeQuery = true)
  Double getAverageStars(Integer bookId);

}
