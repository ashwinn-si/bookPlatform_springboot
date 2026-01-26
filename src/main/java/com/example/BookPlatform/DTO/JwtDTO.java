package com.example.BookPlatform.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtDTO {
    private String userId;
    private String role;
}
