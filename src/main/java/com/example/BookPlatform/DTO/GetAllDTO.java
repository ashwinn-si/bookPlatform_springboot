package com.example.BookPlatform.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetAllDTO <T>{
    private Integer totalPages;
    private Integer currPage;
    private Integer size;
    private List<T> data;

    public GetAllDTO(Integer totalPages, Integer currPage, Integer size, List<T> data){
        this.totalPages = totalPages;
        this.currPage = currPage + 1;
        this.size = size;
        this.data = data;
    }
}
