package com.example.expense;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** Requires a matching X-API-Key header on /expenses. */
@Component
@Order(2)
public class ApiKeyFilter extends OncePerRequestFilter {

    private final byte[] apiKey;

    public ApiKeyFilter(AppProperties props) {
        if (props.apiKey() == null || props.apiKey().isBlank()) {
            throw new IllegalStateException("API_KEY must be set");
        }
        this.apiKey = props.apiKey().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !(path.equals("/expenses") || path.startsWith("/expenses/"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String given = request.getHeader("X-API-Key");
        if (given == null || !MessageDigest.isEqual(apiKey, given.getBytes(StandardCharsets.UTF_8))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"missing or invalid X-API-Key\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}
