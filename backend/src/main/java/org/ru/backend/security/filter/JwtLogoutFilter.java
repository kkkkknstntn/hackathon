package org.ru.backend.security.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ru.backend.entity.DeactivatedToken;
import org.ru.backend.enums.Authorities;
import org.ru.backend.repository.DeactivatedTokenRepository;
import org.ru.backend.security.TokenUser;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtLogoutFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/logout", HttpMethod.POST.name());

    private final DeactivatedTokenRepository deactivatedTokenRepository;



    @Override
    @Transactional
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        var context = SecurityContextHolder.getContext();

        if (context == null || context.getAuthentication() == null) {
            throw new AccessDeniedException("User must be authenticated with JWT");
        }

        if (!(context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken auth)) {
            throw new AccessDeniedException("Invalid authentication context");
        }

        if (!(auth.getPrincipal() instanceof TokenUser user)) {
            throw new AccessDeniedException("Principal must be TokenUser");
        }

        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority(Authorities.JWT_LOGOUT.name()))) {
            throw new AccessDeniedException("Missing JWT_LOGOUT authority");
        }

        deactivatedTokenRepository.save(new DeactivatedToken(
                user.getToken().id(),
                user.getToken().expiresAt()
        ));

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        logger.info("JwtLogoutFilter initialized");
    }
}
