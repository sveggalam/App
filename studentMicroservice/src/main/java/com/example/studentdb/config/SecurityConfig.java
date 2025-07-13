package com.example.studentdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN") // admin
            .build();

        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("user123"))
            .roles("USER") // → user
            .build();
        UserDetails dev = User.builder()
            .username("Subhash")
            .password(passwordEncoder().encode("subhash1234"))
            .roles("USER")
            .build();
         return new InMemoryUserDetailsManager(admin, user,dev);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf-> cross site request forgery
        // 
        http
            .csrf(csrf -> csrf.disable()) // not needed for apis
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/students").hasAnyRole("USER", "ADMIN") // both
                .requestMatchers("/library/register").hasAnyRole("USER","ADMIN")
                .anyRequest().hasRole("ADMIN") //everything else: only admin
            )
            .httpBasic(); // use Basic Auth for testing (deprecated)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
