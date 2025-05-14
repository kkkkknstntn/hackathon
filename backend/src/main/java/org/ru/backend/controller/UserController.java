package org.ru.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ru.backend.dto.user.UserRequestDTO;
import org.ru.backend.dto.user.UserResponseDTO;
import org.ru.backend.service.UserService;
import org.ru.backend.validation.ValidationGroups;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями системы")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать нового пользователя",
            description = "Регистрация нового пользователя в системе"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким именем уже существует")
    })
    public UserResponseDTO createUser(
            @Validated(ValidationGroups.Create.class) @ModelAttribute UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    @Operation(
            summary = "Получить список всех пользователей",
            description = "Требуются права администратора"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пользователей"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Обновить данные пользователя",
            description = "Обновление информации о пользователе (доступно администратору или самому пользователю)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные обновлены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Нет прав на обновление"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @Validated(ValidationGroups.Update.class) @ModelAttribute UserRequestDTO dto) {
        return userService.updateUser(id, dto);
    }

    @Operation(
            summary = "Получить информацию о текущем пользователе",
            description = "Получение данных аутентифицированного пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация получена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public UserResponseDTO getCurrentUser() {
        return userService.getCurrentUser();
    }
}