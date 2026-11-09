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
@DisplayName("Scenariusz 03: Szukanie laptop nie znajduje Laptop Dell, a 100% znajduje za dużo")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("Fragment nazwy bez względu na wielkość liter (name=laptop)")
    void nameFragment() {
        resultsMatch("/api/products/search?name=laptop", p -> p.getName().toLowerCase().contains("laptop"));
    }

    @Test
    @DisplayName("Polskie znaki wielkimi literami (name=ŁÓDKA)")
    void polishCharacters() {
        resultsMatch("/api/products/search?name=%C5%81%C3%93DKA", p -> p.getName().toLowerCase().contains("łódka"));
    }

    @Test
    @DisplayName("Znak % w szukanym tekście oznacza procent (name=100%)")
    void percent() {
        resultsMatch("/api/products/search?name=100%25", p -> p.getName().contains("100%"));
    }

    @Test
    @DisplayName("Znak _ w szukanym tekście oznacza podkreślnik (name=kabel_usb)")
    void underscore() {
        resultsMatch("/api/products/search?name=kabel_usb", p -> p.getName().toLowerCase().contains("kabel_usb"));
    }
}
