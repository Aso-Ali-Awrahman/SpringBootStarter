package com.aso.springstarter.dtos;

import java.util.List;

import lombok.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Value
public class PagingRequest {
    int page;
    int size;
    String sort;
    Sort.Direction direction;

    public PagingRequest(int page, int size, String sort, String direction) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 1);
        this.sort = sort;
        this.direction = !direction.equals("desc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    public Pageable toPageable(List<String> allowedSorts) {
        final var sortProperty = allowedSorts.contains(sort) ? sort : allowedSorts.getFirst();
        final Sort sort = Sort.by(direction, sortProperty);
        return PageRequest.of(page, size, sort);
    }

}
