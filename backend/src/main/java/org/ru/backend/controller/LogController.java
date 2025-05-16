package org.ru.backend.controller;

import lombok.RequiredArgsConstructor;
import org.ru.backend.dto.SearchLogsResponseDTO;
import org.ru.backend.service.LogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
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
     * @return Ответ с логами и списком уникальных пакетов.
     */
    @GetMapping("/searchLogs")
    public SearchLogsResponseDTO searchLogs(@RequestParam(required = false) String query,
                                         @RequestParam(required = false) String programmingLanguage,
                                         @RequestParam(required = false) String errors,
                                         @RequestParam(required = false) String packageField,
                                         @RequestParam(required = false) String packageDependencies,
                                         @RequestParam(required = false) String packageDescription,
                                         @RequestParam(required = false) String packageGroup,
                                         @RequestParam(required = false) String packageSummary,
                                         @RequestParam(required = false) String log) {
        return logService.searchLogsWithPackages(query, programmingLanguage, errors, packageField,
                packageDependencies, packageDescription, packageGroup, packageSummary, log);
    }
}