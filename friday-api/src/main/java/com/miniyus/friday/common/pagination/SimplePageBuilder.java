package com.miniyus.friday.common.pagination;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Pageable;

/**
 * SimplePageBuilder
 * <p>
 * [Generic Builder] - R: Resource Type - T: Element Type
 * </p>
 *
 * @author miniyus
 * @date 2023/09/09
 */
public class SimplePageBuilder<R, T> {
    private List<T> content;
    private List<R> inputList;
    private Function<R, T> map;
    private long totalElements;
    private Pageable pageable;

    public SimplePageBuilder<R, T> content(List<R> content) {
        this.inputList = content;
        return this;
    }

    public SimplePageBuilder<R, T> totalElements(long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public SimplePageBuilder<R, T> pageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    public SimplePageBuilder<R, T> map(Function<R, T> func) {
        this.map = func;
        return this;
    }

    public SimplePage<T> build() {
        this.content = inputList.stream().map(this.map).toList();
        return new SimplePage<T>(
                content,
                totalElements,
                pageable);
    }

    public SimplePage<T> build(String resourceFieldName) {
        this.content = inputList.stream().map(this.map).toList();
        return new SimplePage<T>(
                content,
                totalElements,
                pageable,
                resourceFieldName);
    }
}
