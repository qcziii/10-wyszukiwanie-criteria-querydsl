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
@DisplayName("Scenariusz 07: Po przejściu na Specification stronicowanie kończy się błędem")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("Pierwsza strona po 5 produktów (size=5)")
    void firstPage() {
        Response response = get("/api/products/search?size=5");
        assertThat(response.ok()).as("GET /api/products/search?size=5 zwraca %s", response.description()).isTrue();
        ResultPage<ProductDto> page = page(response);
        assertThat(ids(page)).as("produkty na pierwszej stronie").containsExactly(1L, 2L, 3L, 4L, 5L);
        assertThat(page.totalElements()).as("totalElements").isEqualTo(60);
    }

    @Test
    @DisplayName("Druga strona z filtrem (category=dom&size=3&page=1)")
    void secondPageWithFilter() {
        List<Long> expected = expected(p -> p.getCategory() != null && p.getCategory().getName().equals("Dom"));
        Response response = get("/api/products/search?category=dom&size=3&page=1");
        assertThat(response.ok()).as("GET /api/products/search?category=dom&size=3&page=1 zwraca %s", response.description()).isTrue();
        ResultPage<ProductDto> page = page(response);
        assertThat(ids(page)).as("produkty na drugiej stronie").isEqualTo(expected.subList(3, 6));
        assertThat(page.totalElements()).as("totalElements").isEqualTo(expected.size());
    }

    @Test
    @DisplayName("Strona wyników to najwyżej 3 zapytania SQL (bez N+1)")
    void withoutNPlusOne() {
        Response response = get("/api/products/search?size=20");
        assertThat(response.ok()).as(response.description()).isTrue();
        assertThat(response.sql()).as("zapytania SQL dla jednej strony (strona, tagi, count)").hasSizeLessThanOrEqualTo(3);
    }

    @Test
    @DisplayName("Filtry (nazwa, cena, tagi)")
    void filters() {
        resultsMatch("/api/products/search?name=rower&priceTo=2000",
                p -> p.getName().toLowerCase().contains("rower") && p.getPrice().compareTo(new BigDecimal("2000")) <= 0);
        resultsMatch("/api/products/search?tags=eko,promocja",
                p -> p.getTags().stream().map(Tag::getName).collect(Collectors.toSet()).containsAll(Set.of("eko", "promocja")));
    }
}
