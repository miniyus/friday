package com.meteormin.friday.infrastructure.persistence;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SortQueryDsl.
 *
 * @author meteormin
 * @since 2023/12/27
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SortQueryDsl {
    private SortQueryDsl() {
    }

    /**
     * Creates an array of OrderSpecifier objects based on the given EntityPathBase and Sort.
     *
     * @param qEnt the EntityPathBase representing the query entity
     * @param sort the Sort object representing the sorting criteria
     * @return an array of OrderSpecifier objects
     */
    public static <T extends EntityPathBase<?>> OrderSpecifier<Comparable>[] createOrderSpecifier(
        T qEnt, Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (Objects.isNull(sort)) {
            return orderSpecifiers.toArray(OrderSpecifier[]::new);
        } else {
            for (Sort.Order o : sort) {
                Order direction = o.getDirection().isAscending() ? Order.ASC : Order.DESC;
                PathBuilder<?> path =
                    new PathBuilder<>(qEnt.getType(), o.getProperty());
                orderSpecifiers.add(
                    new OrderSpecifier(direction, path));
            }
        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }
}
