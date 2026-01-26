package com.example.BookPlatform.Utils;

import com.example.BookPlatform.DTO.JwtDTO;
import com.example.BookPlatform.Enums.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static int  jwtExpirationTime = 15 * 3600 * 1000;
    private static String jwtSecret = "7f9a3c2e8b4d6a1f9e0c2b7a8d4f6e9c1a3b5d7e9f0a2c4b6d8e1f3a5";

    public static String  generateJWTToken(long userId, Roles role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .compact();
    }

    public static boolean isValid(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJwt(token)
                    .getBody();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static Claims getTokenClaims(String token){
        return  Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJwt(token)
                .getBody();
    }

}
