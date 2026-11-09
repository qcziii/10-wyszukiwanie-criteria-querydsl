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
@DisplayName("Scenariusz 02: Z dwoma filtrami działa tylko jeden")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("Nazwa i cena naraz (name=laptop&priceTo=3000)")
    void nameAndPrice() {
        resultsMatch("/api/products/search?name=laptop&priceTo=3000",
                p -> p.getName().toLowerCase().contains("laptop") && p.getPrice().compareTo(new BigDecimal("3000")) <= 0);
    }

    @Test
    @DisplayName("Kategoria i zakres cen naraz (category=telefony&priceFrom=500&priceTo=1500)")
    void categoryAndRange() {
        resultsMatch("/api/products/search?category=telefony&priceFrom=500&priceTo=1500",
                p -> p.getCategory() != null && p.getCategory().getName().equals("Telefony")
                        && p.getPrice().compareTo(new BigDecimal("500")) >= 0
                        && p.getPrice().compareTo(new BigDecimal("1500")) <= 0);
    }

    @Test
    @DisplayName("Pojedynczy filtr nadal działa (priceTo=50)")
    void oneFilter() {
        resultsMatch("/api/products/search?priceTo=50", p -> p.getPrice().compareTo(new BigDecimal("50")) <= 0);
    }
}
