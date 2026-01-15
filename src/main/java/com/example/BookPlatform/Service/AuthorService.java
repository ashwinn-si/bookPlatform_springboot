package com.example.BookPlatform.Service;

import com.example.BookPlatform.DTO.GetAllDTO;
import com.example.BookPlatform.Domain.Author;
import com.example.BookPlatform.Domain.Book;
import com.example.BookPlatform.Repository.AuthorRepository;
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
class GetAllAuthorDTO{
    private String name;
    private Integer bookCount;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GetAuthorDTO{
    private String name;
    private List<String> bookNames;
}

@Service
public class AuthorService {

    private  final AuthorRepository authorRepository;

    AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    @Transactional
    public void addAuthor(String name){
        Author author = new Author(name);
        authorRepository.save(author);
    }

    public GetAllDTO getAllAuthor(Integer page, Integer size, String name){
        Page<Author> authorPage;
        List<GetAllAuthorDTO> allAuthorDTOS = new ArrayList<>();
        if(name != null){
            authorPage = authorRepository.findByNameContaining(PageRequest.of(page, size), name);
        }else{
            authorPage = authorRepository.findAll(PageRequest.of(page, size));
        }
        for(Author author: authorPage.getContent()){
            allAuthorDTOS.add(new GetAllAuthorDTO(author.getName(), author.getBookList().size()));
        }
        return new GetAllDTO<>(authorPage.getTotalPages(), page,authorPage.getSize(), allAuthorDTOS);
    }

    public GetAuthorDTO getAuthor(Integer authorId){
        Author author = checkIfAuthorExisits(authorId);
        GetAuthorDTO getAuthorDTO = new GetAuthorDTO();
        getAuthorDTO.setName(author.getName());
        List<String> books = new ArrayList<>();
        for(Book book: author.getBookList()){
            books.add(book.getName());
        }
        getAuthorDTO.setBookNames(books);
        return getAuthorDTO;
    }

    @Transactional
    public void updateAuthor(Integer authorId, String name){
        Author author = checkIfAuthorExisits(authorId);
        author.setName(name);
        authorRepository.save(author);
    }

    @Transactional
    public void deleteAuthor(Integer authorId){
        checkIfAuthorExisits(authorId);
        authorRepository.deleteById(authorId);
    }

    private Author checkIfAuthorExisits(Integer authorId){
        return authorRepository.findById(authorId).orElseThrow(() -> {
            throw new CustomError(HttpStatus.NOT_FOUND, "author not found");
        });
    }
}
