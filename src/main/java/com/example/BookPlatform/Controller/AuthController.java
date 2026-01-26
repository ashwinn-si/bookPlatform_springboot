package com.example.BookPlatform.Controller;

import com.example.BookPlatform.Service.AuthService;
import com.example.BookPlatform.Utils.ResponseHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@AllArgsConstructor
@NoArgsConstructor
class LoginDTO{
    @NotBlank(message = "email is required")
    @Email(message = "email formating is missing")
    private String email;

    @NotBlank(message = "password is required")
    @Length(min = 1, message = "password is required")
    private String password;
}

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {


    private final AuthService authService;

    AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody @Valid LoginDTO loginDTO){
        return ResponseHandler.handleResponse(HttpStatus.OK, authService.loginIn(loginDTO.getEmail(), loginDTO.getPassword()), "login successfull");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody  @Valid LoginDTO loginDTO){
        authService.signUp(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseHandler.handleResponse(HttpStatus.OK, null, "signup successfull");
    }
}
