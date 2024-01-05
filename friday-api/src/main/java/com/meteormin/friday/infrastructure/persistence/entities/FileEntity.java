package com.meteormin.friday.infrastructure.persistence.entities;

import com.meteormin.friday.infrastructure.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE file SET deleted_at = NOW() WHERE id = ?")
public class FileEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 50)
    @NonNull
    private String mimeType;

    @Column(nullable = false)
    @NonNull
    private Long size;

    @Column(nullable = false)
    @NonNull
    private String path;

    @Column(nullable = false)
    @NonNull
    private String originName;

    @Column(nullable = false)
    @NonNull
    private String convName;

    @Column(nullable = false, length = 20)
    @NonNull
    private String extension;

    @Column
    @Nullable
    private LocalDateTime deletedAt;

    @ManyToOne
    private UserEntity user;

    public static FileEntity create(
        @NonNull String mimeType,
        @NonNull Long size,
        @NonNull String path,
        @NonNull String extension
    ) {
        return FileEntity.builder()
            .mimeType(mimeType)
            .size(size)
            .path(path)
            .extension(extension)
            .build();
    }
}
