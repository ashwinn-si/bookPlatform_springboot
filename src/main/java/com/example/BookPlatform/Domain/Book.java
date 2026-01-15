package Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "books")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NotNull(message = "book name is required")
  private String name;

  @NotBlank
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "author_id")
  private Author author;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name ="book_category",
          joinColumns = @JoinColumn(name = "book_id"),
          inverseJoinColumns = @JoinColumn(name="category_id")
  )
  private List<Category> categoryList = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name="book_reviews",
          joinColumns = @JoinColumn(name = "book_id"),
          inverseJoinColumns = @JoinColumn(name = "review_id")
  )
  private List<Review> reviewList = new ArrayList<>();
}
