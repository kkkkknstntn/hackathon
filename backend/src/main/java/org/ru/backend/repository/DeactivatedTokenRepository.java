package org.ru.backend.repository;

import org.ru.backend.entity.DeactivatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DeactivatedTokenRepository extends JpaRepository<DeactivatedToken, UUID> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM DeactivatedToken d WHERE d.id = :tokenId AND d.keepUntil > CURRENT_TIMESTAMP")
    boolean existsActiveDeactivatedToken(@Param("tokenId") UUID tokenId);

    @Query("DELETE FROM DeactivatedToken d WHERE d.keepUntil <= CURRENT_TIMESTAMP")
    void deleteExpiredTokens();
}