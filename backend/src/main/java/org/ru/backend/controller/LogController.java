package org.ru.backend.controller;

import lombok.RequiredArgsConstructor;
import org.ru.backend.document.LogDocument;
import org.ru.backend.dto.SearchLogsResponseDTO;
import org.ru.backend.repository.impl.LogDocumentRepositoryImpl;
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
     * @return Ответ с логами и списком уникальных пакетов.
     */
    @GetMapping("/searchLogs")
    public SearchLogsResponseDTO searchLogs(@RequestParam(required = false) String query,
                                            @RequestParam(required = false) String programmingLanguage,
                                            @RequestParam(required = false) String errors,
                                            @RequestParam(required = false) String packageField) {
        return logService.searchLogsWithPackages(query, programmingLanguage, errors, packageField);
    }
}