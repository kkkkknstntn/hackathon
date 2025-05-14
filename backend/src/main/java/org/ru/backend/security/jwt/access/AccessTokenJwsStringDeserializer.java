package org.ru.backend.security.jwt.access;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.ru.backend.security.Token;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
public class AccessTokenJwsStringDeserializer implements Function<String, Token> {


    private final JWSVerifier jwsVerifier;

    public AccessTokenJwsStringDeserializer(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    public Token apply(String string) {
        log.debug("Deserializing access token: {}", string);
        try {
            var signedJWT = SignedJWT.parse(string);
            if (signedJWT.verify(this.jwsVerifier)) {
                var claimsSet = signedJWT.getJWTClaimsSet();
                return new Token(UUID.fromString(claimsSet.getJWTID()), claimsSet.getSubject(),
                        claimsSet.getStringListClaim("authorities"),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant());
            }
        } catch (ParseException | JOSEException exception) {
            log.error(exception.getMessage(), exception);
        }
        return null;
    }
}
