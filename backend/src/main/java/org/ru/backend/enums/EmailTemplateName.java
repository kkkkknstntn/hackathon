package org.ru.backend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account"),
    RECOVERY_PASSWORD("recovery_password");

    private final String templateName;
}
