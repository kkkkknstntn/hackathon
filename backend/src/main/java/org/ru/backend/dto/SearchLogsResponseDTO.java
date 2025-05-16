package org.ru.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.ru.backend.document.LogDocument;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchLogsResponseDTO {
    private List<String> uniquePackages;
    private List<LogDocument> logs;
}
