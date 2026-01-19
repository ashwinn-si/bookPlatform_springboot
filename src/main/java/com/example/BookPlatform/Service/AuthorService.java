package com.example.BookPlatform.Service;

import com.example.BookPlatform.DTO.GetAllDTO;
import com.example.BookPlatform.Domain.Author;
import com.example.BookPlatform.Domain.Book;
import com.example.BookPlatform.Repository.AuthorRepository;
import com.example.BookPlatform.Repository.BookRepository;
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
class GetAllAuthorDTO {
  private String name;
  private Integer bookCount;
  private Integer authorId;
  private Double avgStars;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GetAuthorDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Book{
        String bookName;
        Integer bookId;
        Double avgStars;
    }
    private String name;
    private List<Book> bookNames;
    private Integer totalPages;
    private Integer currPage;
    private Integer size;
    private Integer authorId;
    private Double avgStars;
}

@Service
public class AuthorService {

  private final AuthorRepository authorRepository;
  private final BookRepository bookRepository;
  AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.bookRepository = bookRepository;
      this.authorRepository = authorRepository;
  }

  @Transactional
  public void addAuthor(String name) {
    Author author = new Author(name);
    authorRepository.save(author);
  }

  public GetAllDTO<GetAllAuthorDTO> getAllAuthor(Integer page, Integer size, String name) {
    Page<Author> authorPage;
    List<GetAllAuthorDTO> allAuthorDTOS = new ArrayList<>();
    PageRequest pageRequest = PageRequest.of(page - 1, size);
    if (name != null) {
      authorPage = authorRepository.findByNameContaining(pageRequest, name);
    } else {
      authorPage = authorRepository.findAll(pageRequest);
    }
    for (Author author : authorPage.getContent()) {
        Double avgStars = authorRepository.getStars(author.getId());
        allAuthorDTOS.add(new GetAllAuthorDTO(author.getName(), author.getBookList().size(), author.getId(), avgStars));
    }
    return new GetAllDTO<GetAllAuthorDTO>(authorPage.getTotalPages(), page, authorPage.getSize(), allAuthorDTOS);
  }

  public GetAuthorDTO getAuthor(Integer authorId, Integer page, Integer size) {
    Author author = checkIfAuthorExisits(authorId);
    PageRequest pageRequest = PageRequest.of(page - 1, size);
    Double avgStars = authorRepository.getStars(author.getId());
    GetAuthorDTO getAuthorDTO = new GetAuthorDTO();
    getAuthorDTO.setName(author.getName());
    List<GetAuthorDTO.Book> books = new ArrayList<>();
    Page<Book> bookList = bookRepository.findByAuthor(author, pageRequest);
    for (Book book : bookList) {
        Double avgStarBook = bookRepository.getAverageStars(book.getId());
        books.add(new GetAuthorDTO.Book(book.getName(), book.getId(), avgStars));
    }
    getAuthorDTO.setBookNames(books);
    getAuthorDTO.setAuthorId(authorId);
    getAuthorDTO.setAvgStars(avgStars);
    getAuthorDTO.setSize(bookList.getSize());
    getAuthorDTO.setCurrPage(page);
    getAuthorDTO.setTotalPages(bookList.getTotalPages());
    return getAuthorDTO;
  }

  @Transactional
  public void updateAuthor(Integer authorId, String name) {
    Author author = checkIfAuthorExisits(authorId);
    author.setName(name);
    authorRepository.save(author);
  }

  @Transactional
  public void deleteAuthor(Integer authorId) {
    checkIfAuthorExisits(authorId);
    authorRepository.deleteById(authorId);
  }

  private Author checkIfAuthorExisits(Integer authorId) {
    return authorRepository.findById(authorId).orElseThrow(() -> {
      throw new CustomError(HttpStatus.NOT_FOUND, "author not found");
    });
  }
}
