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

@Table(name = "authors")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NotBlank(message = "Author name is required")
  @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")
  @Column(nullable = false, length = 100)
  private String name;

  @OneToMany(mappedBy = "author", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
  private List<Book> bookList = new ArrayList<>();

  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
  @JoinTable(name = "author_category", joinColumns = @JoinColumn(name = "author_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
  private List<Category> categories = new ArrayList<>();
}
