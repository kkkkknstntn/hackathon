package org.ru.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeactivatedToken {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "c_keep_until", nullable = false)
    private Instant keepUntil;
}
