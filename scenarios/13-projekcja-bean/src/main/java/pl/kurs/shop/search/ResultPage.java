package pl.kurs.shop.search;

import org.springframework.data.domain.Pageable;

import java.util.List;

/** One page of search results. */
public record ResultPage<T>(List<T> content, long totalElements, int totalPages, int number, int size) {

    public static <T> ResultPage<T> of(List<T> content, long totalElements, Pageable pageable) {
        int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
        return new ResultPage<>(content, totalElements, totalPages, pageable.getPageNumber(), pageable.getPageSize());
    }
}
