package com.example.BookPlatform.Config;

import com.example.BookPlatform.DTO.UserDetailsDTO;
import com.example.BookPlatform.Utils.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilterChain extends OncePerRequestFilter {

    private final JwtService jwtService;

    JwtFilterChain(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(!authHeader.startsWith("Bearer ") || authHeader == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);

        if(!jwtService.isValidToken(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Claims claims = jwtService.getTokenClaims(token);
        Integer userId = claims.get("userId", Integer.class);
        String role = claims.get("role", String.class);

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(userId, role);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetailsDTO, // this is the main contnet
                null, // it is the password field
                List.of() //list of permission the user has
        );
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }

}
