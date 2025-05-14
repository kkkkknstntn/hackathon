package org.ru.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.ru.backend.enums.MailTokenType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mail_token")
public class MailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Enumerated(EnumType.STRING)
    private MailTokenType tokenType;

    private LocalDateTime expiresAt;
    private boolean enabled = true;

    private String email;
}
