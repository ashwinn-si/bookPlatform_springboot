package Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "categories")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NotBlank(message = "Category name is required")
  @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
  @Column(nullable = false, unique = true, length = 50)
  private String name;

  @ManyToMany(mappedBy = "categories")
  private List<Book> bookList = new ArrayList<>();

  @ManyToMany(mappedBy = "categories")
  private List<Author> authorList = new ArrayList<>();
}
