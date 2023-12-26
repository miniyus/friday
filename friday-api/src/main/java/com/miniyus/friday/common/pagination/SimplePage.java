package com.miniyus.friday.common.pagination;

import com.fasterxml.jackson.annotation.*;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SimplePage
 *
 * @param <T> the type parameter
 * @author seongminyoo
 * @since 2023/09/06
 */
@JsonIgnoreProperties({
    "pageable",
    "number",
    "numberOfElements",
    "first",
    "last",
    "empty",
    "content"
})
@EqualsAndHashCode(callSuper = true)
public class SimplePage<T> extends PageImpl<T> {
    private final transient Map<String, List<T>> resource;

    /**
     * For Json Serialization
     *
     * @param resourceName  the resource name
     * @param content       the content
     * @param totalElements the total elements
     * @param totalPages    the total pages
     * @param page          the page
     * @param size          the page size
     * @param sort          the sort
     */
    @JsonCreator
    public SimplePage(
        final String resourceName,
        final List<T> content,
        @JsonProperty("totalElements") final long totalElements,
        @JsonProperty("totalPages") final int totalPages,
        @JsonProperty("page") final int page,
        @JsonProperty("size") final int size,
        @JsonProperty("sort") final List<String> sort) {
        super(content, PageRequest.of(page + 1, size, Sort.by(sort.stream()
            .map(el -> el.split(","))
            .map(ar -> new Sort.Order(Sort.Direction.fromString(ar[1]), ar[0]))
            .toList())), totalElements);
        this.resource = new HashMap<>();
        this.resource.put(resourceName, content);
    }

    /**
     * Create SimplePage, use custom resource name
     *
     * @param content           the content
     * @param totalElements     the total elements
     * @param pageable          the pageable
     * @param resourceFieldName the resource field name
     */
    public SimplePage(
        final List<T> content,
        final long totalElements,
        final Pageable pageable,
        final String resourceFieldName) {
        super(content, pageable, totalElements);
        this.resource = new HashMap<>();
        this.resource.put(resourceFieldName, content);
    }

    /**
     * Create SimplePage, use default resource name(Default: resources)
     *
     * @param content       the content
     * @param totalElements the total elements
     * @param pageable      the pageable
     */
    public SimplePage(
        final List<T> content,
        final long totalElements,
        final Pageable pageable) {
        super(content, pageable, totalElements);
        this.resource = new HashMap<>();
        this.resource.put("resources", content);
    }

    /**
     * Get page number
     *
     * @return page number
     */
    public int getPage() {
        return getNumber() + 1;
    }

    /**
     * Get sort list
     *
     * @return get sort list
     */
    @JsonProperty("sort")
    public List<String> getSortList() {
        return getSort().stream()
            .map(order -> order.getProperty() + "," + order.getDirection().name())
            .toList();
    }

    /**
     * Get resources.
     *
     * @return get resources
     */
    @JsonAnyGetter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Map<String, List<T>> getResource() {
        return resource;
    }

    /**
     * SimplePageBuilder
     *
     * @param <R> Input Data type parameter
     * @param <T> Output Data type parameter
     * @return SimplePageBuilder
     * @see SimplePageBuilder
     */
    public static <R, T> SimplePageBuilder<R, T> builder() {
        return new SimplePageBuilder<>();
    }
}
