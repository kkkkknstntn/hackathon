package org.ru.backend.security.jwt.refresh;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import lombok.extern.slf4j.Slf4j;
import org.ru.backend.security.Token;
import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
public class RefreshTokenJweStringDeserializer implements Function<String, Token> {

    private final JWEDecrypter jweDecrypter;

    public RefreshTokenJweStringDeserializer(JWEDecrypter jweDecrypter) {
        this.jweDecrypter = jweDecrypter;
    }

    @Override
    public Token apply(String string) {
        try {
            var encryptedJWT = EncryptedJWT.parse(string);
            encryptedJWT.decrypt(this.jweDecrypter);
            var claimsSet = encryptedJWT.getJWTClaimsSet();
            return new Token(UUID.fromString(claimsSet.getJWTID()), claimsSet.getSubject(),
                    claimsSet.getStringListClaim("authorities"),
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant());
        } catch (ParseException | JOSEException exception) {
            log.error(exception.getMessage(), exception);
        }

        return null;
    }
}