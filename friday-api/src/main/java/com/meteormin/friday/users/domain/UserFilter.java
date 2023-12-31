package com.meteormin.friday.users.domain;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Builder
public record UserFilter(
    Long userId,
    String email,
    String name,
    LocalDateTime createdAtStart,
    LocalDateTime createdAtEnd,
    LocalDateTime updatedAtStart,
    LocalDateTime updatedAtEnd,
    Pageable pageable
) {

    /**
     * Checks if all the fields in the object are null.
     *
     * @return true if all fields are null, false otherwise
     */
    public boolean isEmpty() {
        return email == null && name == null && createdAtStart == null && createdAtEnd == null
            && updatedAtStart == null && updatedAtEnd == null;
    }
}
