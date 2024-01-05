package com.meteormin.friday.infrastructure.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.meteormin.friday.infrastructure.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "login_history")
public class LoginHistoryEntity extends BaseEntity<Long> {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private boolean success;

    @Column(nullable = false)
    private Integer statusCode;

    @Column(length = 100)
    @Nullable
    private String message;

    @Column(length = 20)
    @Nullable
    private String ip;

    @Nullable
    @Column(columnDefinition = "timestamp")
    private LocalDateTime deletedAt;

    @ManyToOne
    @JsonBackReference
    private UserEntity user;

    public static LoginHistoryEntity create(
        boolean success,
        Integer statusCode,
        String message,
        String ip,
        UserEntity user
    ) {
        return LoginHistoryEntity.builder()
            .success(success)
            .statusCode(statusCode)
            .message(message)
            .ip(ip)
            .user(user)
            .build();
    }
}
