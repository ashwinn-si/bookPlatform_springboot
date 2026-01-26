package com.example.BookPlatform.Service;

import com.example.BookPlatform.Domain.User;
import com.example.BookPlatform.Repository.UserRepository;
import com.example.BookPlatform.Utils.BcryptUtil;
import com.example.BookPlatform.Utils.CustomError;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    UserService(UserRepository userRepository){
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

    public void loginIn(String email, String password){
        Optional<User> existingUser = userRepository.findByEmail(email);
        //TODO email mail logic might be added
        if(existingUser.isEmpty()){
            throw new CustomError(HttpStatus.NOT_FOUND, "user not found");
        }
        if(BcryptUtil.checkPassword(password, existingUser.get().getPassword())) {
            //TODO jwt token generation
            
        }else{
            throw new CustomError(HttpStatus.CONFLICT, "password incorrect");
        }
    }



    private Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
