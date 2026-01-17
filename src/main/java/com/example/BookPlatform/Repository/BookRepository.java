package com.example.BookPlatform.Repository;

import com.example.BookPlatform.Domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
    public Page<Book> findByNameContaining(PageRequest pageRequest, String name);
}
