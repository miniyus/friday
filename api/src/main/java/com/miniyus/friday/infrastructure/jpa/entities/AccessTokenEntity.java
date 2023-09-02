package com.miniyus.friday.infrastructure.jpa.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.miniyus.friday.users.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Entity
@Data
@Where(clause = "revoked = false")
public class AccessTokenEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    private String token;

    private String userId;

    private LocalDateTime expiresAt;

    private boolean revoked;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "accessToken", cascade = CascadeType.ALL)
    private RefreshTokenEntity refreshToken;
}
