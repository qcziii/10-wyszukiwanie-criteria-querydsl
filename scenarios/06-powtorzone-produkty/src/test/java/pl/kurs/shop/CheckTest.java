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
@DisplayName("Scenariusz 06: Po filtrze po ocenie produkty się powtarzają")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("minRating=4: każdy produkt raz i poprawne totalElements")
    void rating4() {
        resultsMatch("/api/products/search?minRating=4", p -> p.getReviews().stream().anyMatch(o -> o.getRating() >= 4));
    }

    @Test
    @DisplayName("minRating=4, strona po 10: 10 różnych produktów, właściwa liczba stron")
    void paging() {
        List<Long> expected = expected(p -> p.getReviews().stream().anyMatch(o -> o.getRating() >= 4));
        Response response = get("/api/products/search?minRating=4&size=10");
        assertThat(response.ok()).as(response.description()).isTrue();
        ResultPage<ProductDto> page = page(response);
        assertThat(ids(page)).as("pierwsza strona (size=10)").isEqualTo(expected.subList(0, 10));
        assertThat(page.totalPages()).as("totalPages").isEqualTo((expected.size() + 9) / 10);
    }

    @Test
    @DisplayName("minRating=5 razem z nazwą")
    void rating5() {
        resultsMatch("/api/products/search?minRating=5&name=a",
                p -> p.getName().toLowerCase().contains("a") && p.getReviews().stream().anyMatch(o -> o.getRating() >= 5));
    }
}
