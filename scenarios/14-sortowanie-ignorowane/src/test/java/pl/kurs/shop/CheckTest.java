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
@DisplayName("Scenariusz 14: Parametr sort w wersji QueryDSL nic nie zmienia")
class CheckTest extends SearchTestBase {

    private List<Long> orders(String url) {
        Response response = get(url);
        assertThat(response.ok()).as("GET %s zwraca %s", url, response.description()).isTrue();
        return ids(page(response));
    }

    @Test
    @DisplayName("QueryDSL: sort=price,desc daje ceny malejąco")
    void priceDescending() {
        Response response = get("/api/products/search-qdsl?sort=price,desc&size=100");
        assertThat(response.ok()).as(response.description()).isTrue();
        assertThat(page(response).content()).extracting(ProductDto::price).as("ceny")
                .hasSize(60).isSortedAccordingTo((a, b) -> b.compareTo(a));
    }

    @Test
    @DisplayName("QueryDSL: ta sama kolejność co w wersji Criteria (nazwa, dodany, kategoria, kilka pól)")
    void sameAsCriteria() {
        for (String sort : List.of("name", "addedAt,desc", "category", "price&sort=name,desc")) {
            String parameters = "?size=100&sort=" + sort;
            assertThat(orders("/api/products/search-qdsl" + parameters)).as("kolejność id dla sort=%s", sort)
                    .isEqualTo(orders("/api/products/search" + parameters));
        }
    }

    @Test
    @DisplayName("QueryDSL: druga strona przy sort=price (size=7&page=1)")
    void secondPage() {
        String parameters = "?sort=price&size=7&page=1";
        assertThat(orders("/api/products/search-qdsl" + parameters)).as("produkty na drugiej stronie")
                .isEqualTo(orders("/api/products/search" + parameters));
    }

    @Test
    @DisplayName("QueryDSL: sort po polu spoza listy (sort=purchasePrice) to błąd 400")
    void disallowedField() {
        Response response = get("/api/products/search-qdsl?sort=purchasePrice");
        assertThat(response.status()).as("status dla sort=purchasePrice (%s)", response.description()).isEqualTo(400);
    }
}
