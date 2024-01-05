package com.meteormin.friday.common.pagination;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;

/**
 * SimplePageBuilder
 * <p>
 * [Generic Builder]
 * </p>
 * <ul>
 *     <li>R: Resource Type </li>
 *     <li>T: Element Type</li>
 * </ul>
 *
 * @author meteormin
 * @since 2023/09/09
 */
public class SimplePageBuilder<R, T> {
    private List<T> content;
    private List<R> inputList;
    private Function<R, T> map;
    private long totalElements;
    private Pageable pageable;

    /**
     * Sets the content of the SimplePageBuilder.
     *
     * @param content the content to be set
     * @return the updated SimplePageBuilder instance
     */
    public SimplePageBuilder<R, T> content(List<R> content) {
        this.inputList = content;
        return this;
    }

    /**
     * Sets the total number of elements in the page.
     *
     * @param totalElements the total number of elements
     * @return the updated SimplePageBuilder instance
     */
    public SimplePageBuilder<R, T> totalElements(long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    /**
     * Sets the pageable object for the SimplePageBuilder.
     *
     * @param pageable the pageable object to be set
     * @return the SimplePageBuilder with the updated pageable object
     */
    public SimplePageBuilder<R, T> pageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    /**
     * A description of the map function.
     *
     * @param func the function to apply to each element
     * @return the updated SimplePageBuilder instance
     */
    public SimplePageBuilder<R, T> map(Function<R, T> func) {
        this.map = func;
        return this;
    }

    /**
     * Builds a SimplePage object.
     *
     * @return A SimplePage object.
     */
    public SimplePage<T> build() {
        this.content = inputList.stream().map(this.map).toList();
        return new SimplePage<>(
            content,
            totalElements,
            pageable);
    }

    /**
     * Builds a SimplePage object based on the input list, total elements, pageable, and resource
     * field name.
     *
     * @param resourceFieldName the name of the resource field
     * @return the constructed SimplePage object
     */
    public SimplePage<T> build(String resourceFieldName) {
        this.content = inputList.stream().map(this.map).toList();
        return new SimplePage<>(
            content,
            totalElements,
            pageable,
            resourceFieldName);
    }
}
