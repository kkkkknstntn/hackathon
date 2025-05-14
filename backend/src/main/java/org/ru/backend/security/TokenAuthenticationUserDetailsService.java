package org.ru.backend.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ru.backend.repository.DeactivatedTokenRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final DeactivatedTokenRepository deactivatedTokenRepository;



    @Transactional(readOnly = true)
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {

        if (!(authenticationToken.getPrincipal() instanceof Token token)) {
            throw new UsernameNotFoundException("Principal must be of type Token");
        }

        boolean isTokenActive = !isTokenDeactivated(token.id()) &&
                token.expiresAt().isAfter(Instant.now());

        return new TokenUser(
                token.subject(),
                "nopassword",
                true,
                true,
                isTokenActive,
                true,
                mapAuthorities(token.authorities()),
                token
        );
    }

    private boolean isTokenDeactivated(UUID tokenId) {
        return deactivatedTokenRepository.existsActiveDeactivatedToken(tokenId);
    }

    private List<GrantedAuthority> mapAuthorities(Collection<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
