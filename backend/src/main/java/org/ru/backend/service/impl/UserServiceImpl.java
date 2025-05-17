package org.ru.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.ru.backend.dto.user.UserRequestDTO;
import org.ru.backend.dto.user.UserResponseDTO;
import org.ru.backend.entity.User;
import org.ru.backend.enums.Authorities;
import org.ru.backend.enums.BusinessErrorCodes;
import org.ru.backend.exception.ApiException;
import org.ru.backend.mapper.UserMapper;
import org.ru.backend.repository.UserRepository;
import org.ru.backend.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {
        userRepository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new ApiException(
                            BusinessErrorCodes.USER_EXISTS,
                            "Username '" + request.getUsername() + "' already exists"
                    );
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.getRoles().add(Authorities.ROLE_USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User newUser = userRepository.save(user);
        return userMapper.toResponseDTO(newUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.USER_NOT_FOUND,
                        "User ID: " + id
                ));

        Optional.ofNullable(dto.getUsername()).ifPresent(user::setUsername);
        Optional.ofNullable(dto.getPassword())
                .ifPresent(pass -> user.setPassword(passwordEncoder.encode(pass)));

        return userMapper.toResponseDTO(userRepository.save(user));
    }


    private Set<Authorities> getRolesFromNames(Set<String> roleNames) {
        return roleNames.stream()
                .map(name -> {
                    try {
                        return Authorities.valueOf(name);
                    } catch (IllegalArgumentException ex) {
                        throw new ApiException(
                                BusinessErrorCodes.INVALID_ROLE,
                                "Invalid role name: " + name
                        );
                    }
                })
                .collect(Collectors.toSet());
    }

    @Override
    public UserResponseDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(
                        BusinessErrorCodes.USER_NOT_FOUND,
                        "Username: " + username
                ));

        return userMapper.toResponseDTO(user);
    }
}