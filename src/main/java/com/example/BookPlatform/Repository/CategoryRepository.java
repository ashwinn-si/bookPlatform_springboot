package com.example.BookPlatform.Repository;

import com.example.BookPlatform.Domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public List<Category> findAllById(List<Integer> id);

    public Optional<Category> findByNameIgnoreCase(String name);

    public Page<Category> findAllByNameIgnoreCase(String name, Pageable pageable);
}
