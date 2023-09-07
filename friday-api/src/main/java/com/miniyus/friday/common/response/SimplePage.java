package com.miniyus.friday.common.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SimpeplePage
 *
 * @author miniyus
 * @param <T>
 * @date 2023/09/06
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
public class SimplePage<T> extends PageImpl<T> {
    private Map<String, List<T>> resource;

    @JsonCreator
    public SimplePage(
            final List<T> content,
            @JsonProperty("totalElements") final long totalElements,
            @JsonProperty("totalPages") final int totalPages,
            @JsonProperty("page") final int page,
            @JsonProperty("size") final int size,
            @JsonProperty("sort") final List<String> sort) {
        super(content, PageRequest.of(page, size, Sort.by(sort.stream()
                .map(el -> el.split(","))
                .map(ar -> new Sort.Order(Sort.Direction.fromString(ar[1]), ar[0]))
                .collect(Collectors.toList()))), totalElements);
        this.resource = new HashMap<String, List<T>>();
        this.resource.put("resources", content);
    }

    @JsonCreator
    public SimplePage(
            final List<T> content,
            @JsonProperty("totalElements") final long totalElements,
            @JsonProperty("totalPages") final int totalPages,
            @JsonProperty("page") final int page,
            @JsonProperty("size") final int size,
            @JsonProperty("sort") final List<String> sort,
            final String resourceFieldName) {
        super(content, PageRequest.of(page, size, Sort.by(sort.stream()
                .map(el -> el.split(","))
                .map(ar -> new Sort.Order(Sort.Direction.fromString(ar[1]), ar[0]))
                .collect(Collectors.toList()))), totalElements);
        this.resource = new HashMap<String, List<T>>();
        this.resource.put(resourceFieldName, content);
    }

    public SimplePage(final List<T> content, final long totalElements, final Pageable pageable) {
        super(content, pageable, totalElements);
    }

    public int getPage() {
        return getNumber();
    }

    @JsonProperty("sort")
    public List<String> getSortList() {
        return getSort().stream()
                .map(order -> order.getProperty() + "," + order.getDirection().name())
                .collect(Collectors.toList());
    }

    @JsonAnyGetter
    public Map<String, List<T>> getResource() {
        return resource;
    }
}