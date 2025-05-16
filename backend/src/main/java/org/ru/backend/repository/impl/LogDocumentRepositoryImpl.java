package org.ru.backend.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.ru.backend.document.LogDocument;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LogDocumentRepositoryImpl {

    private final ElasticsearchClient elasticsearchClient;

    public LogDocumentRepositoryImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    /**
     * Получение логов из Elasticsearch по заданному фильтру.
     *
     * @param query Параметр запроса для поиска по логам.
     * @param programmingLanguage Фильтр по языку программирования.
     * @param errors Фильтр по ошибкам.
     * @param packageField Фильтр по пакету.
     * @return Список логов, соответствующих фильтру.
     */
    public List<LogDocument> searchLogs(String query, String programmingLanguage, String errors, String packageField) {
        try {
            // Создаем запрос для поиска логов
            SearchResponse<LogDocument> response = elasticsearchClient.search(s -> s
                            .index("logs_index")
                            .query(q -> q
                                    .bool(b -> {
                                        if (query != null && !query.isEmpty()) {
                                            b.should(sh -> sh
                                                    .match(m -> m
                                                            .field("log")
                                                            .query(query)
                                                            .fuzziness("AUTO")
                                                    ));
                                        }

                                        if (programmingLanguage != null && !programmingLanguage.isEmpty()) {
                                            b.should(sh -> sh
                                                    .match(m -> m
                                                            .field("programming_language")
                                                            .query(programmingLanguage)
                                                    ));
                                        }

                                        if (errors != null && !errors.isEmpty()) {
                                            b.should(sh -> sh
                                                    .match(m -> m
                                                            .field("errors")
                                                            .query(errors)
                                                    ));
                                        }

                                        if (packageField != null && !packageField.isEmpty()) {
                                            b.should(sh -> sh
                                                    .match(m -> m
                                                            .field("package_field")
                                                            .query(packageField)
                                                    ));
                                        }

                                        return b;
                                    })
                            ),
                    LogDocument.class
            );

            // Извлекаем результаты поиска
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error during search", e);
        }
    }
}
