package org.ru.backend.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ru.backend.config.EmailConfig;
import org.ru.backend.enums.EmailTemplateName;
import org.ru.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Qualifier("emailSender")
public class EmailServiceImpl  implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailConfig emailConfig;


    @Async
    public void sendEmail(String to,
                          String username,
                          EmailTemplateName emailTemplate,
                          String confirmationUrl,
                          String activationCode,
                          String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED
            );

            Map<String, Object> properties = Map.of(
                    "username", username,
                    "confirmationUrl", confirmationUrl,
                    "activation_code", activationCode
            );

            Context context = new Context();
            context.setVariables(properties);

            helper.setFrom(emailConfig.getEmailAddressSender());
            helper.setTo(to);
            helper.setSubject(subject);

            String template = templateEngine.process(emailTemplate.getTemplateName(), context);
            helper.setText(template, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}