package org.devkirby.hanimman.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_address")

public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "primary_address_id", referencedColumnName = "id", nullable = false)
    private Address primaryAddress;

    @ManyToOne
    @JoinColumn(name = "secondly_address_id", referencedColumnName = "id")
    private Address secondlyAddress;

    @Column(name = "validated_at")
    private Instant validatedAt = Instant.now();

    @Column(name = "modified_at")
    private Instant modifiedAt = Instant.now();

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt = Instant.now();

}
