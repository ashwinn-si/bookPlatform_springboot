package com.example.BookPlatform.Service;

import com.example.BookPlatform.DTO.GetAllDTO;
import com.example.BookPlatform.Domain.Book;
import com.example.BookPlatform.Domain.Category;
import com.example.BookPlatform.Repository.CategoryRepository;
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
class CategoryDTO {
  private Integer id;
  private String name;
  private Integer noBooks;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GetCategoryDTO {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class Book {
    Integer id;
    String name;
  }

  private Integer id;
  private String name;
  private List<Book> bookList;
}

@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;

  CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Transactional
  public void addCategory(String name) {
    categoryRepository.findByNameIgnoreCase(name).ifPresent(c -> {
      throw new CustomError(HttpStatus.CONFLICT, "category already exists");
    });
    Category category = new Category(name);
    categoryRepository.save(category);
  }

  @Transactional
  public void deleteCategory(Integer categoryId) {
    isCategoryExists(categoryId);
    categoryRepository.deleteById(categoryId);
  }

  @Transactional
  public void updateCategory(Integer categoryId, String name) {
    Category category = isCategoryExists(categoryId);
    if (name != null) {
      categoryRepository.findByNameIgnoreCase(name).ifPresent(c -> {
        throw new CustomError(HttpStatus.CONFLICT, "category name already exists");
      });
      category.setName(name);
    }
  }

  public GetAllDTO getAllCategory(Integer page, Integer size, String name) {
    PageRequest pageRequest = PageRequest.of(page - 1, size);
    Page<Category> content;
    if (name != null) {
      content = categoryRepository.findAllByNameIgnoreCase(name, pageRequest);
    } else {
      content = categoryRepository.findAll(pageRequest);
    }
    List<CategoryDTO> categories = new ArrayList<>();
    for (Category category : content.getContent()) {
      categories.add(new CategoryDTO(category.getId(), category.getName(), category.getBookList().size()));
    }
    return new GetAllDTO(content.getTotalPages(), page, size, categories);
  }

  public GetCategoryDTO getCategory(Integer categoryId) {
    Category category = isCategoryExists(categoryId);
    GetCategoryDTO getCategoryDTO = new GetCategoryDTO();
    getCategoryDTO.setName(category.getName());
    getCategoryDTO.setId(category.getId());
    List<GetCategoryDTO.Book> bookList = new ArrayList<>();
    for (Book book : category.getBookList()) {
      bookList.add(new GetCategoryDTO.Book(book.getId(), book.getName()));
    }
    getCategoryDTO.setBookList(bookList);
    return getCategoryDTO;
  }

  private Category isCategoryExists(Integer id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new CustomError(HttpStatus.NOT_FOUND, "category not found"));
  }
}