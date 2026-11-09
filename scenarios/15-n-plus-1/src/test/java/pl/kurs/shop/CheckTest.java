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
@DisplayName("Scenariusz 15: Jedno wyszukiwanie wysyła do bazy kilkanaście zapytań")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("QueryDSL: strona 20 produktów to najwyżej 3 zapytania SQL")
    void fewQueries() {
        Response response = get("/api/products/search-qdsl?size=20");
        assertThat(response.ok()).as(response.description()).isTrue();
        assertThat(response.sql()).as("zapytania SQL dla jednej strony (strona, tagi, count)").hasSizeLessThanOrEqualTo(3);
    }

    @Test
    @DisplayName("QueryDSL: kategoria i producent są w wynikach")
    void categoryAndManufacturer() {
        Response response = get("/api/products/search-qdsl?size=100");
        assertThat(response.ok()).as(response.description()).isTrue();
        ResultPage<ProductDto> page = page(response);
        assertThat(page.content()).extracting(ProductDto::category).as("kategorie")
                .containsExactlyElementsOf(fromEachProduct(p -> p.getCategory() == null ? null : p.getCategory().getName()));
        assertThat(page.content()).extracting(ProductDto::manufacturer).as("producenci")
                .containsExactlyElementsOf(fromEachProduct(p -> p.getManufacturer().getName()));
    }

    @Test
    @DisplayName("QueryDSL: filtr i sortowanie po kategorii nadal działają")
    void filter() {
        resultsMatch("/api/products/search-qdsl?category=akcesoria&sort=category",
                p -> p.getCategory() != null && p.getCategory().getName().equals("Akcesoria"));
    }
}
