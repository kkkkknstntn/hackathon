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

    @Field(type = FieldType.Text)
    private String log;

    @Field(name = "programming_language", type = FieldType.Text)
    private String programming_language;

    @Field(type = FieldType.Keyword)
    private List<String> errors;

    @Field(type = FieldType.Text)
    private String package_field;

    @Field(type = FieldType.Date)
    private String timestamp;
}
