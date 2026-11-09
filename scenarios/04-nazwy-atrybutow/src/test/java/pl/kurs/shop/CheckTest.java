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
@DisplayName("Scenariusz 04: Po zmianie nazwy pola wyszukiwarka zwraca 500")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("Filtr ceny (priceFrom=1000&priceTo=2000)")
    void priceFilter() {
        resultsMatch("/api/products/search?priceFrom=1000&priceTo=2000",
                p -> p.getPrice().compareTo(new BigDecimal("1000")) >= 0 && p.getPrice().compareTo(new BigDecimal("2000")) <= 0);
    }

    @Test
    @DisplayName("Sortowanie po cenie malejąco (sort=price,desc)")
    void sortByPrice() {
        Response response = get("/api/products/search?sort=price,desc&size=100");
        assertThat(response.ok()).as("GET /api/products/search?sort=price,desc zwraca %s", response.description()).isTrue();
        List<BigDecimal> ceny = page(response).content().stream().map(ProductDto::price).toList();
        assertThat(ceny).as("ceny po sortowaniu malejąco").hasSize(60).isSortedAccordingTo((a, b) -> b.compareTo(a));
    }

    @Test
    @DisplayName("Pozostałe filtry działają (nazwa, kategoria, tagi, ocena)")
    void otherFilters() {
        resultsMatch("/api/products/search?name=kabel&category=akcesoria",
                p -> p.getName().toLowerCase().contains("kabel") && p.getCategory() != null
                        && p.getCategory().getName().equals("Akcesoria"));
        resultsMatch("/api/products/search?tags=eko&minRating=4",
                p -> p.getTags().stream().anyMatch(t -> t.getName().equals("eko"))
                        && p.getReviews().stream().anyMatch(o -> o.getRating() >= 4));
    }
}
