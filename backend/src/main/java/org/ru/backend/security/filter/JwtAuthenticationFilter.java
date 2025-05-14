package org.ru.backend.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ru.backend.security.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Function<String, Token> accessTokenStringDeserializer;
    private final Function<String, Token> refreshTokenStringDeserializer;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            extractTokenFromRequest(request)
                    .flatMap(this::parseToken)
                    .ifPresent(this::authenticateAndSetSecurityContext);
        } catch (Exception ex) {
            log.error("JWT authentication error", ex);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.replace("Bearer ", ""));
    }

    private Optional<PreAuthenticatedAuthenticationToken> parseToken(String tokenString) {
        return Optional.ofNullable(accessTokenStringDeserializer.apply(tokenString))
                .or(() -> Optional.ofNullable(refreshTokenStringDeserializer.apply(tokenString)))
                .filter(token -> {
                    boolean valid = token.expiresAt().isAfter(java.time.Instant.now());
                    if (!valid) {
                        log.warn("Token is expired: {}", token.id());
                    }
                    return valid;
                })
                .map(token -> {
                    Collection<SimpleGrantedAuthority> authorities = token.authorities()
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    return new PreAuthenticatedAuthenticationToken(
                            token,
                            tokenString,
                            authorities
                    );
                });
    }

    private void authenticateAndSetSecurityContext(PreAuthenticatedAuthenticationToken authenticationToken) {
        try {
            Authentication authenticated = authenticationManager.authenticate(authenticationToken);
            if (authenticated.isAuthenticated()) {
                log.info("Authenticated user: {}", authenticated.getPrincipal());
                org.springframework.security.core.context.SecurityContextHolder.getContext()
                        .setAuthentication(authenticated);
            }
        } catch (AuthenticationException ex) {
            log.error("Authentication failed", ex);
        }
    }
}
