package com.miniyus.friday.common.pagination;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@NoArgsConstructor
@Getter
public class PageRequest {
    @NotNull
    int page;

    @NotNull
    int size;

    @Nullable
    List<String> sort;

    public Pageable toPageable() {
        if (page == 0) {
            page = 1;
        }

        if (size == 0) {
            size = 20;
        }

        if (sort == null || sort.isEmpty()) {
            var sortBy = Sort.by(new Sort.Order(Sort.Direction.DESC, "createdAt"));

            return org.springframework.data.domain.PageRequest.of(
                page - 1,
                size,
                sortBy
            );
        }

        var orders = sort.stream()
            .map(this::mappingOrder).toList();

        return org.springframework.data.domain.PageRequest.of(
            page - 1,
            size,
            Sort.by(orders)
        );
    }

    private Sort.Order mappingOrder(String sortParameter) {
        var split = sortParameter.split(",");

        if (split.length > 1) {
            var direction = Sort.Direction.fromOptionalString(split[1]).orElse(Sort.Direction.ASC);
            return new Sort.Order(direction, split[0]);
        }

        return Sort.Order.by(split[0]);
    }
}
