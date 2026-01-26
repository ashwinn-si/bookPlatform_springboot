package com.example.BookPlatform.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptUtil {
    private static BCryptPasswordEncoder bCryptPasswordEncoder = null;

    BcryptUtil(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public static String generateHashPassword(String originalPassword){
        return bCryptPasswordEncoder.encode(originalPassword);
    }

    public static boolean checkPassword(String enterPassword, String hashPassword){
        return bCryptPasswordEncoder.matches(enterPassword, hashPassword);
    }
}
