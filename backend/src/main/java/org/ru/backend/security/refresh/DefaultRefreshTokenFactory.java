package org.ru.backend.security.refresh;


import org.ru.backend.enums.Authorities;
import org.ru.backend.security.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;
import java.util.function.Function;

public class DefaultRefreshTokenFactory implements Function<Authentication, Token> {

    private Duration tokenTtl = Duration.ofDays(1);

    @Override
    public Token apply(Authentication authentication) {
        var authorities = new LinkedList<String>();
        authorities.add(Authorities.JWT_REFRESH.name());
        authorities.add(Authorities.JWT_LOGOUT.name());
        authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> "GRANT_" + authority)
                .forEach(authorities::add);

        var now = Instant.now();
        return new Token(UUID.randomUUID(), authentication.getName(), authorities, now, now.plus(this.tokenTtl));
    }

    public void setTokenTtl(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }
}
