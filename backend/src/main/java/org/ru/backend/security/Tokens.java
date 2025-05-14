package org.ru.backend.security;

public record Tokens(String accessToken, String accessTokenExpiry,
                     String refreshToken, String refreshTokenExpiry) {
}