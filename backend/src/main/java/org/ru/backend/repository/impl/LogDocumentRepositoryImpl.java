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
     * @param packageDependencies Фильтр по зависимостям пакетов.
     * @param packageDescription Фильтр по описанию пакетов.
     * @param packageGroup Фильтр по группе пакетов.
     * @param packageSummary Фильтр по сводке пакетов.
     * @param log Фильтр по содержимому лога.
     * @return Список логов, соответствующих фильтру.
     */
    public List<LogDocument> searchLogs(String query, String programmingLanguage, String errors, String packageField,
                                        String packageDependencies, String packageDescription, String packageGroup,
                                        String packageSummary, String log) {
        try {
            // Создаем запрос для поиска логов с фильтрами
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
                                                    .nested(n -> n
                                                            .path("errors")
                                                            .query(nq -> nq
                                                                    .bool(bq -> bq
                                                                            .should(shl -> shl
                                                                                    .match(m -> m
                                                                                            .field("errors.full_error")
                                                                                            .query(errors)
                                                                                    ))
                                                                    )
                                                            )
                                                    ));
                                        }

                                        if (packageField != null && !packageField.isEmpty()) {
                                            b.should(sh -> sh
                                                    .match(m -> m
                                                            .field("package_field")
                                                            .query(packageField)
                                                    ));
                                        }

                                        if (packageDependencies != null && !packageDependencies.isEmpty()) {
                                            b.should(sh -> sh
                                                    .match(m -> m
                                                            .field("package_dependencies")
                                                            .query(packageDependencies)
                                                    ));
                                        }

                                        if (packageDescription != null && !packageDescription.isEmpty()) {
                                            b.should(sh -> sh
                                                    .match(m -> m
                                                            .field("package_description")
                                                            .query(packageDescription)
                                                    ));
                                        }

                                        if (packageGroup != null && !packageGroup.isEmpty()) {
                                            b.should(sh -> sh
                                                    .match(m -> m
                                                            .field("package_group")
                                                            .query(packageGroup)
                                                    ));
                                        }

                                        if (packageSummary != null && !packageSummary.isEmpty()) {
                                            b.should(sh -> sh
                                                    .match(m -> m
                                                            .field("package_summary")
                                                            .query(packageSummary)
                                                    ));
                                        }

                                        if (log != null && !log.isEmpty()) {
                                            b.should(sh -> sh
                                                    .match(m -> m
                                                            .field("log")
                                                            .query(log)
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
