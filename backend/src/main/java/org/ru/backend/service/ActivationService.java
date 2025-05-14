package org.ru.backend.service;

import org.ru.backend.entity.User;

public interface ActivationService {

    String generateAndSaveActivationToken(String email);

    void activateAccount(String token, String email);

    void sendActivationEmail(User user);
}