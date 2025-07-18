// package com.example.apiGateWay.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.web.server.ServerHttpSecurity; // Changed for WebFlux
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.NoOpPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.server.SecurityWebFilterChain; // Changed for WebFlux
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;

// @Configuration
// @EnableMethodSecurity // Note: @EnableMethodSecurity works differently in WebFlux vs Servlet, ensure it meets your needs
// public class SecurityConfig {

//     @Bean
//     public UserDetailsService users() {
//         UserDetails admin = User.builder()
//             .username("admin")
//             .password(passwordEncoder().encode("admin123"))
//             .roles("ADMIN","USER")
//             .build();

//         UserDetails user = User.builder()
//             .username("user")
//             .password(passwordEncoder().encode("user123"))
//             .roles("USER")
//             .build();
//         UserDetails dev = User.builder()
//             .username("Subhash")
//             .password(passwordEncoder().encode("subhash1234"))
//             .roles("USER")
//             .build();
//          return new InMemoryUserDetailsManager(admin, user, dev);
//     }

//     @Bean
//     public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) { // Changed HttpSecurity to ServerHttpSecurity
//         // csrf-> cross site request forgery
//         //
//         http
//             .csrf(ServerHttpSecurity.CsrfSpec::disable) // Reactive way to disable CSRF
//             .authorizeExchange(auth -> auth // Use authorizeExchange for reactive
//                 .pathMatchers("/students/**").hasAnyRole("USER", "ADMIN")
//                 .pathMatchers("/library/register").hasAnyRole("USER","ADMIN")
//                 .pathMatchers("/students/actuator/**").permitAll()
//                 .anyExchange().hasAnyRole("USER","ADMIN") //everything else: only user and admin can access
//             )
//             .httpBasic(org.springframework.security.config.Customizer.withDefaults()); // Updated for deprecation

//         return http.build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         // return new BCryptPasswordEncoder();
//         return NoOpPasswordEncoder.getInstance();
//     }
// }