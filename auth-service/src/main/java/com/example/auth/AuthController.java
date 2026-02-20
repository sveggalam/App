package com.example.auth;

import org.springframework.web.bind.annotation.*;

import com.example.auth.dto.LoginRequest;

import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Mono<Map<String, String>> login(@RequestBody LoginRequest request) {

        String token = jwtUtil.generateToken(request.getUsername());

        return Mono.just(Map.of("token", token));
    }
}