package org.ru.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ru.backend.entity.User;
import org.ru.backend.enums.EmailTemplateName;
import org.ru.backend.repository.UserRepository;
import org.ru.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Qualifier("userUpdater")
public class EmailServiceUserUpdaterImpl implements EmailService {
    private final UserRepository userRepository;

    @Override
    public void sendEmail(String to,
                             String username,
                             EmailTemplateName emailTemplate,
                             String confirmationUrl,
                             String activationCode,
                             String subject) {
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

            user.setIsEnabled(true);
            userRepository.save(user);

            log.info("User with username {} is enabled", username);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new RuntimeException("Failed to update user", e);
        }
    }
}
