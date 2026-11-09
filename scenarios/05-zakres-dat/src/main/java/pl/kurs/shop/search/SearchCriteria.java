package pl.kurs.shop.search;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Search parameters from the URL, e.g. {@code ?name=laptop&priceTo=3000&tags=eko,promocja}.
 * Every parameter is optional: a missing (or empty) parameter means "do not filter by it".
 *
 * @param name       name fragment, case-insensitive
 * @param category   exact category name, case-insensitive
 * @param priceFrom  minimum price (inclusive)
 * @param priceTo    maximum price (inclusive)
 * @param addedFrom  first day on which the product could have been added (inclusive)
 * @param addedTo    last day on which the product could have been added (inclusive, the whole day)
 * @param minRating  only products that have at least one review with this or a higher rating
 * @param tags       the product must have <b>all</b> of the given tags
 */
public record SearchCriteria(
        String name,
        String category,
        BigDecimal priceFrom,
        BigDecimal priceTo,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate addedFrom,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate addedTo,
        Integer minRating,
        List<String> tags) {
}
