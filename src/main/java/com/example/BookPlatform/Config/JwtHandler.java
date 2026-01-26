package com.example.BookPlatform.Config;

import com.example.BookPlatform.DTO.JwtDTO;
import com.example.BookPlatform.Enums.Roles;
import com.example.BookPlatform.Utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerToken = request.getHeader("Authorization");
        String url = request.getRequestURI();
        if(!(url.contains("protected") || url.contains("admin"))){
            filterChain.doFilter(request, response);
            return;
        }
        if(headerToken.length() == 0 || !headerToken.startsWith("Bearer")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String token = headerToken.substring(7);
        if(!JwtUtil.isValid(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Claims claims = JwtUtil.getTokenClaims(token);
        long userId = claims.get("userId", Long.class);
        Roles role = claims.get("role", Roles.class);
        JwtDTO jwtDTO = new JwtDTO(userId, role);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                jwtDTO,
                null,
                List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }
}
