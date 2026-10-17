package pl.kurs.sklep.wyszukiwanie;

import org.springframework.data.domain.Pageable;

import java.util.List;

/** Jedna strona wyników wyszukiwania. */
public record Strona<T>(List<T> content, long totalElements, int totalPages, int number, int size) {

    public static <T> Strona<T> z(List<T> content, long totalElements, Pageable pageable) {
        int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
        return new Strona<>(content, totalElements, totalPages, pageable.getPageNumber(), pageable.getPageSize());
    }
}
