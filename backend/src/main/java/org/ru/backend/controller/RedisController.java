package org.ru.backend.controller;

import lombok.RequiredArgsConstructor;
import org.ru.backend.service.RedisService;
import org.ru.backend.enums.RedisKeyEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    /**
     * Эндпоинт для получения всех значений из Redis по ключу.
     * @param enumKey Название ключа (используем енамку).
     * @return Сет значений, соответствующих ключу.
     */
    @Operation(
            summary = "Получить все значения из Redis по ключу",
            description = "Этот эндпоинт позволяет получить все значения, хранящиеся в Redis под заданным ключом."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос, возвращены данные из Redis"),
            @ApiResponse(responseCode = "400", description = "Неверный параметр ключа (enumKey)"),
            @ApiResponse(responseCode = "404", description = "Не найдено значений по указанному ключу")
    })
    @GetMapping("/api/filters")
    public Set<String> getFilters(
            @RequestParam("enumKey")
            @Parameter(
                    description = "Ключ в Redis, соответствующий значению enumKey",
                    required = true,
                    schema = @Schema(allowableValues = {"PACKAGES", "ERRORS"})  // Добавляем возможные значения из Enum
            )
            String enumKey
    ) {
        try {
            // Преобразуем строку в енамку RedisKeyEnum
            RedisKeyEnum redisKeyEnum = RedisKeyEnum.valueOf(enumKey.toUpperCase());

            // Получаем данные по ключу
            return redisService.getValuesByKey(redisKeyEnum.getKey());
        } catch (IllegalArgumentException e) {
            // Если передан некорректный ключ
            e.printStackTrace();
            return null;  // Можно вернуть ошибку или пустой набор
        }
    }

    /**
     * Эндпоинт для получения статистики по ошибкам.
     * Статистика включает:
     * 1. Количество встреч ошибки.
     * 2. Список пакетов, в которых эта ошибка встречалась.
     * @return Статистика по ошибкам.
     */
    @Operation(
            summary = "Получить статистику по ошибкам",
            description = "Этот эндпоинт позволяет получить статистику по ошибкам, включая количество встреч и список пакетов."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос, возвращены данные по ошибкам"),
            @ApiResponse(responseCode = "404", description = "Не найдено ошибок в Redis")
    })
    @GetMapping("/api/errorStats")
    public Map<String, Map<String, Object>> getErrorStats() {
        return redisService.getErrorStats();
    }
}
