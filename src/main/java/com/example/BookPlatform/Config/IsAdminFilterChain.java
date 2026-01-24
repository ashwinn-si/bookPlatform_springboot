package com.example.BookPlatform.Config;

import com.example.BookPlatform.DTO.RoleEnum;
import com.example.BookPlatform.DTO.UserDetailsDTO;
import com.example.BookPlatform.Domain.User;
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
public class IsAdminFilterChain extends OncePerRequestFilter {
    private final UserRepository userRepository;

    IsAdminFilterChain(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO)  authentication.getPrincipal();
        Integer userId = userDetailsDTO.getUserId();

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if(!user.get().getRole().equals(RoleEnum.ADMIN)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
