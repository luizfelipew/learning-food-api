package com.works.foodapi.core.data;

import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.stream.Collectors;

public class PageableTranslator {

    public static Pageable translate(final Pageable pageable, final Map<String, String> fieldsMapping) {
        val orders = pageable.getSort()
                .stream()
                .filter(order ->
                        fieldsMapping.containsKey(order.getProperty()))
                .map(order ->
                        new Sort.Order(
                                order.getDirection(),
                                fieldsMapping.get(order.getProperty())
                        )
                )
                .collect(Collectors.toList());

        return PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(orders));
    }
}
