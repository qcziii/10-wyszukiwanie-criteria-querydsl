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
@DisplayName("Scenariusz 09: Po dodaniu metamodelu Hibernate kompilacja nie widzi QProduct")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("Obie wyszukiwarki działają i zwracają to samo")
    void bothSearches() {
        for (String url : List.of("/api/products/search", "/api/products/search-qdsl")) {
            resultsMatch(url + "?name=rower", p -> p.getName().toLowerCase().contains("rower"));
            resultsMatch(url + "?tags=eko&priceTo=100",
                    p -> p.getTags().stream().anyMatch(t -> t.getName().equals("eko")) && p.getPrice().compareTo(new BigDecimal("100")) <= 0);
        }
    }
}
