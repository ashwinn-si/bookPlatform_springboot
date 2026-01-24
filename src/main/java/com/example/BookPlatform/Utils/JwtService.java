package com.example.BookPlatform.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    private Integer expirationTime = 3600000;
    private String jwtSecret = "ashwinisagoodboy";

    public String generateJWTToken(Integer userId, String role){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();
    }

    public boolean isValidToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJwt(jwtSecret)
                    .getBody();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public Claims getTokenClaims(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJwt(jwtSecret)
                .getBody();
    }

}
