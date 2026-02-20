package com.example.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Mono<Map<String, String>> login(@RequestParam String username) {
        String token = jwtUtil.generateToken(username);
        return Mono.just(Map.of("token", token));
    }
}
