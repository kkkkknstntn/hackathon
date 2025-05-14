package org.ru.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.sql.Update;
import org.ru.backend.validation.ValidationGroups;
import org.springframework.web.multipart.MultipartFile;


@Data
public class UserRequestDTO {
    @NotBlank(message = "Username is required", groups = {ValidationGroups.Create.class})
    @Size(min = 3, max = 50, groups = {ValidationGroups.Create.class, Update.class})
    @Email(message = "Invalid email format", groups = {ValidationGroups.Create.class})
    private String username;

    @NotBlank(message = "Password is required", groups = ValidationGroups.Create.class)
    @Size(min = 8, groups = ValidationGroups.Create.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private MultipartFile imageFile;
}
