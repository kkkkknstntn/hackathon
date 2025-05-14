package org.ru.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO implements Serializable {
    Long id;
    String username;
    String imagePath;
    Set<String> roles;
    LocalDateTime createdAt;
}

