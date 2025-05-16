package org.ru.backend.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "logs_index")
public class LogDocument {

    @Field(name = "programming_language", type = FieldType.Text)
    private String programming_language;

    @Field(type = FieldType.Nested) // Используем Nested тип для сложных объектов, как errors
    private List<Error> errors;

    @Field(type = FieldType.Text)
    private String package_field;

    @Field(type = FieldType.Date)
    private String timestamp;

    @Field(name = "package_dependencies", type = FieldType.Text)
    private List<String> package_dependencies;

    @Field(name = "package_description", type = FieldType.Text)
    private String package_description;

    @Field(name = "package_group", type = FieldType.Text)
    private String package_group;

    @Field(name = "package_summary", type = FieldType.Text)
    private String package_summary;

    @Field(type = FieldType.Text)
    private String log;

    @Field(type = FieldType.Date)
    private String first_log_date;

    @Field(type = FieldType.Date)
    private String last_log_date;


    // Вложенный класс для ошибок
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        @Field(type = FieldType.Text)
        private String full_error;

        @Field(type = FieldType.Text)
        private String short_name;
    }
}
