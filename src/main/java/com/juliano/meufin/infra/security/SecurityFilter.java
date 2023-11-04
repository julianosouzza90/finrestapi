package com.juliano.meufin.infra.security;
import com.juliano.meufin.infra.exception.UnauthorizedException;
import com.juliano.meufin.repository.UserRepository;
import com.juliano.meufin.util.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.UUID;

@Component
public class SecurityFilter  extends OncePerRequestFilter {

    private UserRepository userRepository;
    private TokenService tokenService;
    public SecurityFilter(
            UserRepository userRepository,
            TokenService tokenService
    ) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException {
        var token = getToken(request);
        if(token != null) {
            var userId = this.tokenService.validateToken(token);
            UserDetails user = this.userRepository.getUserById(UUID.fromString(userId));
            if(user != null) {
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

        }


        filterChain.doFilter(request, response);

    }

    private String getToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ","").trim();
        }

        return null;

    }
}
