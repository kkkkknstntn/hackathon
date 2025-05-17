package org.ru.backend.service;

import org.ru.backend.dto.SearchLogsResponseDTO;

public interface LogService {
    SearchLogsResponseDTO searchLogsWithPackages(
            String query,
            String programmingLanguage,
            String errors,
            String packageField,
            String packageDependencies,
            String packageDescription,
            String packageGroup,
            String packageSummary,
            String log,
            String date
    );
}