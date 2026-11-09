package pl.kurs.shop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.kurs.shop.model.Product;
import pl.kurs.shop.model.Tag;
import pl.kurs.shop.search.ProductDto;
import pl.kurs.shop.search.ResultPage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Tests that check the scenario. They are run by check.sh. Do not change this file. */
@DisplayName("Scenariusz 01: Z pustym polem kategoria wyszukiwarka nic nie zwraca")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("Pusty parametr kategoria nie filtruje (category=&priceTo=100)")
    void emptyCategory() {
        resultsMatch("/api/products/search?name=&category=&priceTo=100",
                p -> p.getPrice().compareTo(new BigDecimal("100")) <= 0);
    }

    @Test
    @DisplayName("Kategoria z samych spacji też nie filtruje")
    void categoryOfSpaces() {
        resultsMatch("/api/products/search?category=%20%20&priceFrom=2000", p -> p.getPrice().compareTo(new BigDecimal("2000")) >= 0);
    }

    @Test
    @DisplayName("Filtr kategorii nadal działa (category=ogród)")
    void categoryFilter() {
        resultsMatch("/api/products/search?category=ogród",
                p -> p.getCategory() != null && p.getCategory().getName().equals("Ogród"));
    }
}
