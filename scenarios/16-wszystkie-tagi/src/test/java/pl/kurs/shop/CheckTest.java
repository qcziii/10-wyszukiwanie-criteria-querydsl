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
@DisplayName("Scenariusz 16: Filtr tags=eko,promocja zwraca produkty z którymkolwiek tagiem")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("Criteria: tags=eko,promocja to produkty z OBOMA tagami")
    void criteriaTwoTags() {
        resultsMatch("/api/products/search?tags=eko,promocja", p -> p.getTags().stream().map(Tag::getName).collect(Collectors.toSet()).containsAll(Set.of("eko", "promocja")));
    }

    @Test
    @DisplayName("QueryDSL: tags=eko,promocja to produkty z OBOMA tagami")
    void querydslTwoTags() {
        resultsMatch("/api/products/search-qdsl?tags=eko,promocja", p -> p.getTags().stream().map(Tag::getName).collect(Collectors.toSet()).containsAll(Set.of("eko", "promocja")));
    }

    @Test
    @DisplayName("Obie wersje: trzy tagi i wielkość liter (tags=EKO,promocja,nowosc)")
    void threeTags() {
        for (String url : List.of("/api/products/search", "/api/products/search-qdsl")) {
            resultsMatch(url + "?tags=EKO,promocja,nowosc", p -> p.getTags().stream().map(Tag::getName).collect(Collectors.toSet()).containsAll(Set.of("eko", "promocja", "nowosc")));
        }
    }

    @Test
    @DisplayName("Obie wersje: jeden tag, strona po 5, poprawne totalElements")
    void oneTagPaging() {
        List<Long> expected = expected(p -> p.getTags().stream().anyMatch(t -> t.getName().equals("promocja")));
        for (String url : List.of("/api/products/search", "/api/products/search-qdsl")) {
            Response response = get(url + "?tags=promocja&size=5&page=1");
            assertThat(response.ok()).as(response.description()).isTrue();
            ResultPage<ProductDto> page = page(response);
            assertThat(ids(page)).as("%s: druga strona", url).isEqualTo(expected.subList(5, 10));
            assertThat(page.totalElements()).as("%s: totalElements", url).isEqualTo(expected.size());
        }
    }
}
