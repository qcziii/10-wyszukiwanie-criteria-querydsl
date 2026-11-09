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
@DisplayName("Scenariusz 11: Produkty bez kategorii znikają z wyników QueryDSL")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("QueryDSL: bez filtrów są wszystkie 60 produktów, też te bez kategorii")
    void allProducts() {
        ResultPage<ProductDto> page = resultsMatch("/api/products/search-qdsl", p -> true);
        assertThat(page.content()).extracting(ProductDto::name).as("produkty bez kategorii")
                .contains("Bon rabatowy 100%", "Router Wi-Fi 6");
    }

    @Test
    @DisplayName("QueryDSL: liczba produktów na stronach zgadza się z totalElements (size=10, strona 6)")
    void lastPage() {
        Response response = get("/api/products/search-qdsl?size=10&page=5");
        assertThat(response.ok()).as(response.description()).isTrue();
        ResultPage<ProductDto> page = page(response);
        assertThat(page.totalElements()).as("totalElements").isEqualTo(60);
        assertThat(ids(page)).as("produkty na stronie 6 z 6").containsExactly(51L, 52L, 53L, 54L, 55L, 56L, 57L, 58L, 59L, 60L);
    }

    @Test
    @DisplayName("QueryDSL: filtry i sortowanie po kategorii (priceFrom=500&sort=category)")
    void withFilter() {
        ResultPage<ProductDto> page = resultsMatch("/api/products/search-qdsl?sort=category&priceFrom=500",
                p -> p.getPrice().compareTo(new BigDecimal("500")) >= 0);
        List<String> categories = page.content().stream().map(ProductDto::category).filter(k -> k != null).toList();
        assertThat(categories).as("nazwy kategorii po kolei").isSorted();
    }

    @Test
    @DisplayName("QueryDSL: filtr kategorii nadal działa (category=sport)")
    void categoryFilter() {
        resultsMatch("/api/products/search-qdsl?category=sport",
                p -> p.getCategory() != null && p.getCategory().getName().equals("Sport"));
    }
}
