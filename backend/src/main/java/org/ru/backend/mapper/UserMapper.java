package org.ru.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.ru.backend.dto.user.UserRequestDTO;
import org.ru.backend.dto.user.UserResponseDTO;
import org.ru.backend.entity.User;
import org.ru.backend.enums.Authorities;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract User toEntity(UserRequestDTO dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "authoritiesToRoles")
    public abstract UserResponseDTO toResponseDTO(User user);

    @Named("rolesToAuthorities")
    public Set<Authorities> rolesToAuthorities(Set<String> roles) {
        return roles.stream()
                .map(role -> {
                    try {
                        return Authorities.valueOf(role.toUpperCase());
                    } catch (IllegalArgumentException ex) {
                        throw new IllegalArgumentException("Invalid role: " + role);
                    }
                })
                .collect(Collectors.toSet());
    }

    @Named("authoritiesToRoles")
    public Set<String> authoritiesToRoles(Set<Authorities> authorities) {
        return authorities.stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}
