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
@DisplayName("Scenariusz 10: Filtr ceny w wersji QueryDSL jest ignorowany")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("QueryDSL: zakres cen (priceFrom=1000&priceTo=2000)")
    void priceRange() {
        resultsMatch("/api/products/search-qdsl?priceFrom=1000&priceTo=2000",
                p -> p.getPrice().compareTo(new BigDecimal("1000")) >= 0 && p.getPrice().compareTo(new BigDecimal("2000")) <= 0);
    }

    @Test
    @DisplayName("QueryDSL: cena razem z nazwą (name=kabel&priceTo=40)")
    void priceAndName() {
        resultsMatch("/api/products/search-qdsl?name=kabel&priceTo=40",
                p -> p.getName().toLowerCase().contains("kabel") && p.getPrice().compareTo(new BigDecimal("40")) <= 0);
    }

    @Test
    @DisplayName("QueryDSL: pozostałe filtry (kategoria, daty, tagi)")
    void remaining() {
        resultsMatch("/api/products/search-qdsl?category=sport&addedFrom=2026-04-05&tags=promocja",
                p -> p.getCategory() != null && p.getCategory().getName().equals("Sport")
                        && !p.getAddedAt().isBefore(LocalDateTime.of(2026, 4, 5, 0, 0))
                        && p.getTags().stream().anyMatch(t -> t.getName().equals("promocja")));
    }
}
