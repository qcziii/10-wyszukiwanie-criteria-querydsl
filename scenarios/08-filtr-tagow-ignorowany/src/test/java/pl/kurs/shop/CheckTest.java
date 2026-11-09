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
@DisplayName("Scenariusz 08: Filtr po tagu zwraca wszystkie produkty")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("Jeden tag (tags=outlet)")
    void oneTag() {
        resultsMatch("/api/products/search?tags=outlet", p -> p.getTags().stream().anyMatch(t -> t.getName().equals("outlet")));
    }

    @Test
    @DisplayName("Dwa tagi naraz (tags=eko,promocja)")
    void twoTags() {
        resultsMatch("/api/products/search?tags=eko,promocja",
                p -> p.getTags().stream().map(Tag::getName).collect(Collectors.toSet()).containsAll(Set.of("eko", "promocja")));
    }

    @Test
    @DisplayName("Tag razem z kategorią (tags=eko&category=dom)")
    void tagAndCategory() {
        resultsMatch("/api/products/search?tags=eko&category=dom",
                p -> p.getTags().stream().anyMatch(t -> t.getName().equals("eko"))
                        && p.getCategory() != null && p.getCategory().getName().equals("Dom"));
    }

    @Test
    @DisplayName("Nieistniejący tag nie znajduje nic (tags=xyz)")
    void nonExistentTag() {
        resultsMatch("/api/products/search?tags=xyz", p -> false);
    }
}
