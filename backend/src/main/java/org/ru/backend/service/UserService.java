package org.ru.backend.service;

import org.ru.backend.dto.user.UserRequestDTO;
import org.ru.backend.dto.user.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO createUser(UserRequestDTO request);
    UserResponseDTO updateUser(Long id, UserRequestDTO dto);
    UserResponseDTO getCurrentUser();
}