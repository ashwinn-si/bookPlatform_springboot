package com.example.BookPlatform.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtFilterChain jwtFilterChain;
    private final IsAdminFilterChain isAdminFilterChain;

    SecurityConfig(JwtFilterChain jwtFilterChain, IsAdminFilterChain isAdminFilterChain){
        this.jwtFilterChain = jwtFilterChain;
        this.isAdminFilterChain = isAdminFilterChain;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/protected/**").authenticated()
                                .anyRequest().permitAll()
                ).addFilterBefore(jwtFilterChain, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(isAdminFilterChain, JwtFilterChain.class);
        return httpSecurity.build();
    }
}
