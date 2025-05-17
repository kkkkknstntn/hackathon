package org.ru.backend.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.ru.backend.document.LogDocument;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;
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
     * @param date Фильтр по дате, между first_log_date и last_log_date.
     * @return Список логов, соответствующих фильтру.
     */
    public List<LogDocument> searchLogs(String query, String programmingLanguage, String errors, String packageField,
                                        String packageDependencies, String packageDescription, String packageGroup,
                                        String packageSummary, String log, String date) {
        try {
            SearchResponse<LogDocument> response = elasticsearchClient.search(s -> s
                            .index("logs_index")
                            .query(q -> q
                                    .bool(b -> {
                                        // Используем must вместо should для обязательных условий
                                        if (query != null && !query.isEmpty()) {
                                            b.must(sh -> sh
                                                    .match(m -> m
                                                            .field("log")
                                                            .query(query)
                                                            .fuzziness("AUTO")
                                                    ));
                                        }

                                        if (programmingLanguage != null && !programmingLanguage.isEmpty()) {
                                            b.must(sh -> sh
                                                    .match(m -> m
                                                            .field("programming_language")
                                                            .query(programmingLanguage)
                                                    ));
                                        }

                                        if (errors != null && !errors.isEmpty()) {
                                            b.must(sh -> sh
                                                    .nested(n -> n
                                                            .path("errors")
                                                            .query(nq -> nq
                                                                    .bool(bq -> bq
                                                                            .must(shl -> shl
                                                                                    .matchPhrase(m -> m
                                                                                            .field("errors.short_name")
                                                                                            .query(errors)
                                                                                    ))
                                                                    )
                                                            )
                                                    ));
                                        }

                                        if (packageField != null && !packageField.isEmpty()) {
                                            b.must(sh -> sh
                                                    .matchPhrase(m -> m
                                                            .field("package_field")
                                                            .query(packageField)
                                                    ));
                                        }

                                        if (packageDependencies != null && !packageDependencies.isEmpty()) {
                                            b.must(sh -> sh
                                                    .match(m -> m
                                                            .field("package_dependencies")
                                                            .query(packageDependencies)
                                                    ));
                                        }

                                        if (packageDescription != null && !packageDescription.isEmpty()) {
                                            b.must(sh -> sh
                                                    .match(m -> m
                                                            .field("package_description")
                                                            .query(packageDescription)
                                                    ));
                                        }

                                        if (packageGroup != null && !packageGroup.isEmpty()) {
                                            b.must(sh -> sh
                                                    .match(m -> m
                                                            .field("package_group")
                                                            .query(packageGroup)
                                                    ));
                                        }

                                        if (packageSummary != null && !packageSummary.isEmpty()) {
                                            b.must(sh -> sh
                                                    .match(m -> m
                                                            .field("package_summary")
                                                            .query(packageSummary)
                                                    ));
                                        }

                                        if (log != null && !log.isEmpty()) {
                                            b.must(sh -> sh
                                                    .matchPhrase(m -> m
                                                            .field("log")
                                                            .query(log) // Ищем точное вхождение фразы в том же порядке
                                                    )
                                            );
                                        }


                                        // Исправляем фильтрацию по дате: объединяем оба условия через must
                                        if (date != null && !date.isEmpty()) {
                                            b.must(sh -> sh
                                                    .bool(dateBool -> dateBool
                                                            .must(range -> range
                                                                    .range(r -> r
                                                                            .field("first_log_date")
                                                                            .lte(JsonData.of(date))
                                                                    )
                                                            )
                                                            .must(range -> range
                                                                    .range(r -> r
                                                                            .field("last_log_date")
                                                                            .gte(JsonData.of(date))
                                                                    )
                                                            )
                                                    )
                                            );
                                        }

                                        return b;
                                    })
                            ),
                    LogDocument.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error during search", e);
        }
    }
}
