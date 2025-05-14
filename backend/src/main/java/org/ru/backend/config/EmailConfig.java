package org.ru.backend.config;

import lombok.Data;
import org.ru.backend.repository.UserRepository;
import org.ru.backend.service.EmailService;
import org.ru.backend.service.impl.EmailServiceImpl;
import org.ru.backend.service.impl.EmailServiceUserUpdaterImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@ConfigurationProperties(prefix = "mailing")
@Data
@Configuration
public class EmailConfig {
    private String activationUrl;
    private String emailAddressSender;
    private long activationTokenExpiration = 60;

    @Value("${emailServiceImplementation}")
    private String emailServiceImplementation;

    @Bean
    public EmailService emailService(JavaMailSender mailSender,
                                     SpringTemplateEngine templateEngine,
                                     EmailConfig emailConfig,
                                     UserRepository userRepository) {

        if ("userUpdater".equals(emailServiceImplementation)) {
            return new EmailServiceUserUpdaterImpl(userRepository);
        }
        return new EmailServiceImpl(mailSender, templateEngine, emailConfig);
    }
}
