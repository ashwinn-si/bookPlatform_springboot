package com.example.BookPlatform.Utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
public class BcryptUtil {
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    BcryptUtil(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String generateHashPassword(String originalPassword){
        String hashPassword  = bCryptPasswordEncoder.encode(originalPassword);
        return hashPassword;
    }

    public boolean checkPassword(String enterPassword, String hashPassword){
        return bCryptPasswordEncoder.matches(enterPassword, hashPassword);
    }
}
