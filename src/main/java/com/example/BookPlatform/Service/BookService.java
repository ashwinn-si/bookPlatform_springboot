package com.example.BookPlatform.Service;

import com.example.BookPlatform.DTO.GetAllDTO;
import com.example.BookPlatform.Domain.Author;
import com.example.BookPlatform.Domain.Book;
import com.example.BookPlatform.Domain.Category;
import com.example.BookPlatform.Domain.Review;
import com.example.BookPlatform.Repository.AuthorRepository;
import com.example.BookPlatform.Repository.BookRepository;
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
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class BookDTO {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class Author {
    private String name;
    private Integer authorId;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class Category{
      private String name;
      private Integer categoryId;
  }
  private String name;
  private Integer bookId;
  private Author author;
  private Double stars;
  private List<Category> categoryList;
}

@Service
public class BookService {
  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;
  private final CategoryRepository categoryRepository;

  BookService(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
    this.categoryRepository = categoryRepository;
  }

  public GetAllDTO<BookDTO> getAllBook(Integer page, Integer size, String name) {
    PageRequest pageRequest = PageRequest.of(page - 1, size);
    Page<Book> bookList;
    if (name == null) {
      bookList = bookRepository.findAll(pageRequest);
    } else {
      bookList = bookRepository.findByNameContaining(pageRequest, name);
    }
    List<BookDTO> returnDTO = new ArrayList<>();
    for (Book book : bookList.getContent()) {
      Author author = book.getAuthor();
        Double avgStars = bookRepository.getAverageStars(book.getId());
        returnDTO.add(new BookDTO(book.getName(), book.getId(), new BookDTO.Author(author.getName(), author.getId()), avgStars, null));
    }
    return new GetAllDTO<BookDTO>(bookList.getTotalPages(), page, size, returnDTO);
  }

  public BookDTO getBook(Integer bookId) {
    Book book = isBookExsits(bookId);
    Author author = book.getAuthor();
    Double avgStars = bookRepository.getAverageStars(book.getId());
    List<BookDTO.Category> categoryList = new ArrayList<>();
    for(Category category: book.getCategoryList()){
        categoryList.add(new BookDTO.Category(category.getName(), category.getId()));
    }
    return new BookDTO(book.getName(), book.getId(), new BookDTO.Author(author.getName(), author.getId()),
        avgStars, categoryList);
  }

  @Transactional
  public void addBook(String name, Integer authorId, List<Integer> category) {
    Author author = isAuthorExists(authorId);
    List<Category> categoryList = getCategory(category);
    if (categoryList.size() != category.size()) {
      throw new CustomError(HttpStatus.NOT_FOUND, "one or more categories not found");
    }
    Book book = new Book(name, categoryList);
    book.setAuthor(author);
    bookRepository.save(book);
  }

  @Transactional
  public void deleteBook(Integer bookId) {
    isBookExsits(bookId);
    bookRepository.deleteById(bookId);
  }

  @Transactional
  public void updateBook(Integer bookId, Integer authorId, String name, List<Integer> categories) {
    if (authorId == null && name == null) {
      throw new CustomError(HttpStatus.CONFLICT, "new author or new book name is required for update");
    }
    Book book = isBookExsits(bookId);
    if (authorId != null) {
      Author author = isAuthorExists(authorId);
      book.setAuthor(author);
    }
    if (name != null) {
      book.setName(name);
    }
    if (categories != null) {
      List<Category> categoryList = getCategory(categories);
      if (categoryList.size() != categories.size()) {
        throw new CustomError(HttpStatus.NOT_FOUND, "one or more categories not found");
      }
      book.setCategoryList(categoryList);
    }
    bookRepository.save(book);
  }

  private Book isBookExsits(Integer bookId) {
    return bookRepository.findById(bookId).orElseThrow(() -> new CustomError(HttpStatus.NOT_FOUND, "book not found"));
  }

  private Author isAuthorExists(Integer authorId) {
    return authorRepository.findById(authorId)
        .orElseThrow(() -> new CustomError(HttpStatus.NOT_FOUND, "author not found"));
  }

  private List<Category> getCategory(List<Integer> categories) {
    List<Category> categoryList = new ArrayList<>();
    for (Integer id : categories) {
      Category category = isCategoryExists(id);
      categoryList.add(category);
    }
    return categoryList;
  }

  private Category isCategoryExists(Integer categoryId) {
    return categoryRepository.findById(categoryId).orElseThrow(
        () -> new CustomError(HttpStatus.NOT_FOUND, "category missing"));
  }
}
