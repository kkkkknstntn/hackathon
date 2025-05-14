package org.ru.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {
    String accessToken;
    String accessExpiresAt;
    String refreshToken;
    String refreshExpiresAt;
}
