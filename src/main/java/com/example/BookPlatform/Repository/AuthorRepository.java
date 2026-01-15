package com.example.BookPlatform.Repository;

import com.example.BookPlatform.Domain.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
  public Optional<Author> findByName(String name);

  public Page<Author> findByNameContaining(PageRequest pageRequest, String name);
}
