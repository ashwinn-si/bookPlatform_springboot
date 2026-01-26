package com.example.BookPlatform.Config;

import com.example.BookPlatform.DTO.JwtDTO;
import com.example.BookPlatform.Domain.User;
import com.example.BookPlatform.Enums.Roles;
import com.example.BookPlatform.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class IsAdminHandler extends OncePerRequestFilter {
    private final UserRepository userRepository;

    IsAdminHandler(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        System.out.println(url);
        if(!(url.contains("protected") || url.contains("admin"))){
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Authentication authentication = securityContextHolder.getAuthentication();
        JwtDTO jwtDTO = (JwtDTO) authentication.getPrincipal();

        Optional<User> user = userRepository.findById(jwtDTO.getUserId());
        if(user.isEmpty()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if(user.get().getRole() != Roles.ADMIN){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
