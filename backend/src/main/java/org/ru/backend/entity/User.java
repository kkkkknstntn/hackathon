package org.ru.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.ru.backend.enums.Authorities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"users\"")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    @Column(nullable = false)
    private Boolean isEnabled = true;

    private LocalDateTime createdAt;


    @Column(name = "imagePath")
    private String imagePath;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Authorities> roles = new HashSet<>();
}
