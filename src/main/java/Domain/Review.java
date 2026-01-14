package Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "reviews")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @NotBlank(message = "Review message is required")
  @Size(min = 10, max = 1000, message = "Review message must be between 10 and 1000 characters")
  @Column(nullable = false, length = 1000)
  private String message;

  @NotNull(message = "Star rating is required")
  @Min(value = 1, message = "Rating must be at least 1 star")
  @Max(value = 5, message = "Rating must be at most 5 stars")
  @Column(nullable = false)
  private Integer stars;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;
}
