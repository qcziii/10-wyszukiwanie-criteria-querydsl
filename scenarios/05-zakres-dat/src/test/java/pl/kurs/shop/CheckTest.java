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
@DisplayName("Scenariusz 05: Raport dodane do 31.03 nie zawiera produktów z 31.03")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("Marzec: addedFrom=2026-03-01&addedTo=2026-03-31 obejmuje cały 31 marca")
    void march() {
        resultsMatch("/api/products/search?addedFrom=2026-03-01&addedTo=2026-03-31",
                p -> !p.getAddedAt().isBefore(LocalDateTime.of(2026, 3, 1, 0, 0))
                        && p.getAddedAt().isBefore(LocalDateTime.of(2026, 4, 1, 0, 0)));
    }

    @Test
    @DisplayName("Jeden dzień: addedFrom=addedTo=2026-03-31")
    void oneDay() {
        resultsMatch("/api/products/search?addedFrom=2026-03-31&addedTo=2026-03-31",
                p -> p.getAddedAt().toLocalDate().equals(java.time.LocalDate.of(2026, 3, 31)));
    }

    @Test
    @DisplayName("Produkt dodany 1 kwietnia o północy nie należy do marca")
    void midnight() {
        Response response = get("/api/products/search?addedTo=2026-03-31&size=100");
        assertThat(response.ok()).as(response.description()).isTrue();
        assertThat(page(response).content()).extracting(ProductDto::name)
                .as("produkty dodane do 31 marca").doesNotContain("Laptop Samsung Galaxy Book");
    }
}
