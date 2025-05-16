package org.ru.backend.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum RedisKeyEnum {
    @Schema(description = "Ключ для уникальных пакетов")
    PACKAGES("unique_packages"),
    @Schema(description = "Ключ для уникальных коротких ошибок")
    ERRORS("unique_errors");

    private final String key;

    RedisKeyEnum(String key) {
        this.key = key;
    }

}
