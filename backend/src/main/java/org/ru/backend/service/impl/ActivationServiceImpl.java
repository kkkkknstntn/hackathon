package org.ru.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.ru.backend.config.EmailConfig;
import org.ru.backend.entity.MailToken;
import org.ru.backend.entity.User;
import org.ru.backend.enums.BusinessErrorCodes;
import org.ru.backend.enums.EmailTemplateName;
import org.ru.backend.enums.MailTokenType;
import org.ru.backend.exception.ApiException;
import org.ru.backend.repository.MailTokenRepository;
import org.ru.backend.repository.UserRepository;
import org.ru.backend.service.ActivationService;
import org.ru.backend.service.EmailService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivationServiceImpl implements ActivationService {
    private final MailTokenRepository mailTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailConfig emailConfig;

    @Override
    public String generateAndSaveActivationToken(String email) {
        deactivateExistingTokens(email, MailTokenType.CONFIRM);

        String generatedToken = generateActivationCode(6);
        MailToken mailToken = new MailToken();
        mailToken.setToken(generatedToken);
        mailToken.setExpiresAt(LocalDateTime.now().plusSeconds(
                emailConfig.getActivationTokenExpiration()
        ));
        mailToken.setEmail(email);
        mailToken.setTokenType(MailTokenType.CONFIRM);
        mailTokenRepository.save(mailToken);
        return generatedToken;
    }

    @Override
    public void activateAccount(String token, String email) {
        MailToken savedMailToken = mailTokenRepository.findByEmailAndEnabledAndToken(email, true, token)
                .orElseThrow(() -> new ApiException(BusinessErrorCodes.INVALID_ACTIVATION_TOKEN));

        if (LocalDateTime.now().isAfter(savedMailToken.getExpiresAt())) {
            throw new ApiException(BusinessErrorCodes.EXPIRED_ACTIVATION_TOKEN);
        }
        userRepository.findByUsername(savedMailToken.getEmail()).ifPresent(this::enableUser);
        mailTokenRepository.save(savedMailToken);
    }

    private void enableUser(User user) {
        user.setIsEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void sendActivationEmail(User user) {
        String token = generateAndSaveActivationToken(user.getUsername());
        emailService.sendEmail(
                user.getUsername(),
                user.getUsername(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                emailConfig.getActivationUrl() + "?email=" + user.getUsername() + "&token=" + token,
                token,
                "Account Activation"
        );
    }

    private void deactivateExistingTokens(String email, MailTokenType tokenType) {
        List<MailToken> activeTokens = mailTokenRepository.findByEmailAndEnabledAndTokenType(
                email,
                true,
                tokenType
        );
        activeTokens.forEach(token -> token.setEnabled(false));
        mailTokenRepository.saveAll(activeTokens);
    }

    private String generateActivationCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(secureRandom.nextInt(10));
        }
        return code.toString();
    }
}