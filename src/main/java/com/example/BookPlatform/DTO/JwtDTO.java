package com.example.BookPlatform.DTO;

import com.example.BookPlatform.Enums.Roles;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtDTO {
    private Long userId;
    private Roles role;
}
