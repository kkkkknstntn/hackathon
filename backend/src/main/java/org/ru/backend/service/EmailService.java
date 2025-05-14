package org.ru.backend.service;

import org.ru.backend.enums.EmailTemplateName;

public interface EmailService {
    void sendEmail(String to,
                   String username,
                   EmailTemplateName emailTemplate,
                   String confirmationUrl,
                   String activationCode,
                   String subject);
}
