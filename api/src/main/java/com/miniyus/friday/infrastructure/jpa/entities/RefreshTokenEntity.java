package com.miniyus.friday.infrastructure.jpa.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "revoked = false")
@Table(name = "refresh_token")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    private String token;

    private LocalDateTime expiresAt;

    private boolean revoked;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "access_token_id")
    private AccessTokenEntity accessToken;
}
