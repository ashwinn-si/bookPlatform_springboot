package com.example.BookPlatform.Repository;

import com.example.BookPlatform.Domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Page<Review> findByBookId(PageRequest pageRequest, Integer bookId);
}
