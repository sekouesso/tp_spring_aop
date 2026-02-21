package com.x.sentinel.config;

import com.x.sentinel.utils.SimulatedUserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SecuritySimulationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String userHeader = request.getHeader("X-User");
        String username = (userHeader != null && !userHeader.isBlank()) ? userHeader : "anonymous";
        System.out.println("username "+username);
        List<String> roles = username.equals("admin") ?
                List.of("ADMIN", "USER") :
                List.of("USER");

        SimulatedUserContext.setCurrentUser(new SimulatedUserContext.SimulatedUser(username, roles));

        try {
            filterChain.doFilter(request, response);
        } finally {
            SimulatedUserContext.clear();
        }
    }
}
