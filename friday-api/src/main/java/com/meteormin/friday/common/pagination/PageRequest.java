package com.meteormin.friday.common.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "PageRequest", description = "Page Request")
public class PageRequest {
    @NotNull
    @Schema(description = "Page", example = "1")
    protected int page;

    @NotNull
    @Schema(description = "Page Size", example = "1")
    protected int size;

    @Nullable
    @Schema(description = """
        Sort field. Multiple fields can be provided. The default sort is 'createdAt,desc'.
        queryString ex) ?sort=createdAt,desc&sort=updatedAt,desc
        """, example = "createdAt,desc")
    protected Sort sort;

    public Pageable toPageable() {
        if (sort == null || sort.isEmpty()) {
            var sortBy = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));

            return org.springframework.data.domain.PageRequest.of(
                page - 1,
                size,
                sortBy);
        }

        return org.springframework.data.domain.PageRequest.of(
            page - 1,
            size,
            sort);
    }

    public static Pageable of(@NonNull Pageable pageable) {
        return PageRequest.builder()
            .page(pageable.getPageNumber())
            .size(pageable.getPageSize())
            .sort(pageable.getSort())
            .build()
            .toPageable();
    }
}
