package org.ru.backend.controller;

import lombok.RequiredArgsConstructor;
import org.ru.backend.dto.SearchLogsResponseDTO;
import org.ru.backend.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LogController {

    private final LogService logService;

    /**
     * Метод для поиска логов с агрегацией уникальных пакетов.
     *
     * @param query Параметр запроса для поиска по логам.
     * @param programmingLanguage Фильтр по языку программирования.
     * @param errors Фильтр по ошибкам.
     * @param packageField Фильтр по пакету.
     * @param packageDependencies Фильтр по зависимостям пакетов.
     * @param packageDescription Фильтр по описанию пакетов.
     * @param packageGroup Фильтр по группе пакетов.
     * @param packageSummary Фильтр по сводке пакетов.
     * @param log Фильтр по содержимому лога.
     * @param date Фильтр по дате.
     * @return Ответ с логами и списком уникальных пакетов.
     */
    @Operation(
            summary = "Поиск логов с агрегацией уникальных пакетов",
            description = "Этот эндпоинт выполняет поиск логов с возможностью фильтрации по различным параметрам, включая язык программирования, ошибки, пакеты и другие метаданные."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный поиск логов"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping("/searchLogs")
    public SearchLogsResponseDTO searchLogs(
            @RequestParam(required = false)
            @Parameter(description = "Параметр запроса для нечеткого поиска по логам")
            String query,

            @RequestParam(required = false)
            @Parameter(description = "Фильтр по языку программирования")
            String programmingLanguage,

            @RequestParam(required = false)
            @Parameter(description = "Фильтр по ошибкам")
            String errors,

            @RequestParam(required = false)
            @Parameter(description = "Фильтр по пакету")
            String packageField,

            @RequestParam(required = false)
            @Parameter(description = "Фильтр по зависимостям пакетов")
            String packageDependencies,

            @RequestParam(required = false)
            @Parameter(description = "Фильтр по описанию пакетов")
            String packageDescription,

            @RequestParam(required = false)
            @Parameter(description = "Фильтр по группе пакетов")
            String packageGroup,

            @RequestParam(required = false)
            @Parameter(description = "Фильтр по сводке пакетов")
            String packageSummary,

            @RequestParam(required = false)
            @Parameter(description = "Фильтр по содержимому лога (точное вхождение)")
            String log,

            @RequestParam(required = false)
            @Parameter(description = "Фильтр по дате")
            String date
    ) {
        return logService.searchLogsWithPackages(query, programmingLanguage, errors, packageField,
                packageDependencies, packageDescription, packageGroup, packageSummary, log, date);
    }
}
