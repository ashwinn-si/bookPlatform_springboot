package com.example.BookPlatform.Domain;

import com.example.BookPlatform.DTO.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity(name = "userId")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
