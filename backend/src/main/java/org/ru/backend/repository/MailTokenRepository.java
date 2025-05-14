package org.ru.backend.repository;

import org.ru.backend.entity.MailToken;
import org.ru.backend.enums.MailTokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MailTokenRepository extends JpaRepository<MailToken, Long> {
    Optional<MailToken> findByEmailAndEnabledAndToken(String email, boolean enabled, String token);

    List<MailToken> findByEmailAndEnabledAndTokenType(String email, boolean b, MailTokenType tokenType);
}