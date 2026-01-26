package com.example.BookPlatform.Service;

import com.example.BookPlatform.Domain.User;
import com.example.BookPlatform.Repository.UserRepository;
import com.example.BookPlatform.Utils.BcryptUtil;
import com.example.BookPlatform.Utils.CustomError;
import com.example.BookPlatform.Utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class LoginDTO{
        private String jwtToken;
    }
    private final UserRepository userRepository;

    AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void signUp(String email, String password){
        Optional<User> existingUser = userRepository.findByEmail(email);
        //TODO email mail logic might be added
        if(existingUser.isPresent()){
            throw new CustomError(HttpStatus.CONFLICT, "already user with email exists");
        }
        String hashPassword = BcryptUtil.generateHashPassword(password);
        User user = new User(email, hashPassword);
        userRepository.save(user);
    }

    public LoginDTO loginIn(String email, String password){
        Optional<User> existingUser = userRepository.findByEmail(email);
        //TODO email mail logic might be added for login notification
        if(existingUser.isEmpty()){
            throw new CustomError(HttpStatus.NOT_FOUND, "user not found");
        }
        if(BcryptUtil.checkPassword(password, existingUser.get().getPassword())) {
            return new LoginDTO(JwtUtil.generateJWTToken(existingUser.get().getId(), existingUser.get().getRole()));
        }else{
            throw new CustomError(HttpStatus.CONFLICT, "password incorrect");
        }
    }



    private Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
