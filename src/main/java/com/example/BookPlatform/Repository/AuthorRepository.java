package com.example.BookPlatform.Repository;

import com.example.BookPlatform.Domain.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
  public Optional<Author> findByName(String name);

  public Page<Author> findByNameContaining(PageRequest pageRequest, String name);

  @Query(value = "SELECT \n" +
          "\tAVG(r.stars) \n" +
          "FROM \n" +
          "\tbooks b\n" +
          "JOIN\n" +
          "\treviews r\n" +
          "ON\n" +
          "\tb.id = r.book_id\n" +
          "WHERE\n" +
          "\tb.author_id = ?1\n" +
          ";", nativeQuery = true)
    public Double getStars(Integer authorId);

}
