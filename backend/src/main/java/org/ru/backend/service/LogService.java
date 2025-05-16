package org.ru.backend.service;

import lombok.RequiredArgsConstructor;
import org.ru.backend.document.LogDocument;
import org.ru.backend.dto.SearchLogsResponseDTO;
import org.ru.backend.repository.impl.LogDocumentRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogDocumentRepositoryImpl logDocumentRepository;

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
    public SearchLogsResponseDTO searchLogsWithPackages(String query, String programmingLanguage, String errors, String packageField,
                                                     String packageDependencies, String packageDescription, String packageGroup,
                                                     String packageSummary, String log) {
        // Получаем логи с заданными фильтрами
        List<LogDocument> logs = logDocumentRepository.searchLogs(query, programmingLanguage, errors, packageField,
                packageDependencies, packageDescription, packageGroup, packageSummary, log);

        // Получаем список уникальных пакетов через агрегацию
        List<String> uniquePackages = getUniquePackages(logs);

        return new SearchLogsResponseDTO(uniquePackages, logs);
    }
    /**
     * Получение уникальных пакетов из списка логов.
     *
     * @param logs Список логов для агрегации пакетов.
     * @return Список уникальных пакетов.
     */
    private List<String> getUniquePackages(List<LogDocument> logs) {
        // Используем Set для сбора уникальных значений
        return logs.stream()
                .map(LogDocument::getPackage_field)  // Извлекаем имя пакета
                .filter(packageField -> packageField != null && !packageField.isEmpty())  // Отфильтровываем null или пустые значения
                .distinct()  // Собираем только уникальные
                .collect(Collectors.toList());
    }
}