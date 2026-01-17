package com.example.BookPlatform.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllDTO<T> {
  private Integer totalPages;
  private Integer currPage;
  private Integer size;
  private List<T> data;

}
