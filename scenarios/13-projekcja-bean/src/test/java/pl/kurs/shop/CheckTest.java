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
@DisplayName("Scenariusz 13: Na liście podpowiedzi nazwy produktów to nazwy kategorii")
class CheckTest extends SearchTestBase {

    record Summary(Long id, String name, BigDecimal price, String category) {
    }

    private List<Summary> list() {
        Response response = get("/api/products/list");
        assertThat(response.ok()).as("GET /api/products/list zwraca %s", response.description()).isTrue();
        return list(response, Summary.class);
    }

    @Test
    @DisplayName("Lista: nazwy produktów są nazwami produktów")
    void names() {
        assertThat(list()).extracting(Summary::name).as("nazwy na liście")
                .containsExactlyElementsOf(fromEachProduct(Product::getName));
    }

    @Test
    @DisplayName("Lista: kategorie są wypełnione (null tylko dla produktów bez kategorii)")
    void categories() {
        assertThat(list()).extracting(Summary::category).as("kategorie na liście")
                .containsExactlyElementsOf(fromEachProduct(p -> p.getCategory() == null ? null : p.getCategory().getName()));
    }

    @Test
    @DisplayName("Lista: id i ceny")
    void idsAndPrices() {
        List<Summary> list = list();
        assertThat(list).extracting(Summary::id).as("id").containsExactlyElementsOf(fromEachProduct(Product::getId));
        assertThat(list).extracting(Summary::price).as("ceny").usingElementComparator(BigDecimal::compareTo)
                .containsExactlyElementsOf(fromEachProduct(Product::getPrice));
    }
}
