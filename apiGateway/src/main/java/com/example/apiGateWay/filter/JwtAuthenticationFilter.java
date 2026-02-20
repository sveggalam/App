package com.example.apiGateWay.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    // Inject the JWT secret key from application properties
    @Value("${jwt.secret}")
    private String SECRET;

    // Override the filter method from GlobalFilter interface
    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        // Extract the request path from the URI
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Request path: " + path);
        // 1️⃣ Allow auth endpoints without token validation
        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        // 2️⃣ Extract the Authorization header from the request
        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        // Check if Authorization header is missing or doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Set response status to 401 Unauthorized
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            // Complete the response without forwarding the request
            return exchange.getResponse().setComplete();
        }

        // Extract the JWT token by removing "Bearer " prefix (7 characters)
        String token = authHeader.substring(7);

        try {
            // 3️⃣ Validate and parse the JWT token using the secret key
            Jwts.parserBuilder()
                    // Set the signing key for verification
                    .setSigningKey(Keys.hmacShaKeyFor(
                            SECRET.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    // Parse and validate the token signature
                    .parseClaimsJws(token);

        } catch (Exception e) {
            // If token validation fails, set status to 401 Unauthorized
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            // Complete the response without forwarding the request
            return exchange.getResponse().setComplete();
        }

        // 4️⃣ Forward the request to the next filter/service if token is valid
        return chain.filter(exchange);
    }

    // Override getOrder to ensure this filter runs early in the filter chain
    @Override
    public int getOrder() {
        // Negative order means higher priority (runs earlier)
        return -1;
    }
}
