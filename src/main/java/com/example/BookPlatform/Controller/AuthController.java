package com.example.BookPlatform.Controller;

import com.example.BookPlatform.Service.AuthService;
import com.example.BookPlatform.Utils.ResponseHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class LoginDTO{
        @NotNull(message = "email is required")
        @Email(message = "email formating is missing")
        private String email;

        @NotNull(message = "password is required")
        @Length(min = 0, message = "password is required")
        private String password;
    }

    private final AuthService authService;

    AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@Valid LoginDTO loginDTO){
        return ResponseHandler.handleResponse(HttpStatus.OK, authService.loginIn(loginDTO.getEmail(), loginDTO.getPassword()), "login successfull");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid LoginDTO loginDTO){
        //TODO might change the logic after email
        return ResponseHandler.handleResponse(HttpStatus.OK, authService.loginIn(loginDTO.getEmail(), loginDTO.getPassword()), "signup successfull");
    }
}
