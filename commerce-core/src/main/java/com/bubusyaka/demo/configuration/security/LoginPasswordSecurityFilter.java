package com.bubusyaka.demo.configuration.security;

import com.bubusyaka.demo.model.enums.Role;
import com.bubusyaka.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Set;

@Component
@AllArgsConstructor
public class LoginPasswordSecurityFilter extends OncePerRequestFilter {

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     *
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var authParts = new String(Base64.getDecoder().decode(authHeader.replace("Basic ", ""))).split(":");
        var login = authParts[0];
        var user = userService.loadUserByUsername(login);
        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }
        var password = authParts[1];
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            var authentication = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
