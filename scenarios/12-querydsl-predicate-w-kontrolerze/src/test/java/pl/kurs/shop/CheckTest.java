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
@DisplayName("Scenariusz 12: Filtr nie znajduje lapt, za to pozwala filtrować po cenie zakupu")
class CheckTest extends SearchTestBase {

    @Test
    @DisplayName("Filtr: fragment nazwy bez względu na wielkość liter (name=LAPT)")
    void nameFragment() {
        resultsMatch("/api/products/filter?name=LAPT", p -> p.getName().toLowerCase().contains("lapt"));
    }

    @Test
    @DisplayName("Filtr: fragment nazwy kategorii (category.name=ogr)")
    void category() {
        resultsMatch("/api/products/filter?category.name=ogr",
                p -> p.getCategory() != null && p.getCategory().getName().toLowerCase().contains("ogr"));
    }

    @Test
    @DisplayName("Filtr: dokładna cena (price=99.99)")
    void price() {
        resultsMatch("/api/products/filter?price=99.99", p -> p.getPrice().compareTo(new BigDecimal("99.99")) == 0);
    }

    @Test
    @DisplayName("Filtr: parametr cenaZakupu jest ignorowany (pole poufne)")
    void purchasePrice() {
        resultsMatch("/api/products/filter?purchasePrice=0.00", p -> true);
        resultsMatch("/api/products/filter?name=rower&purchasePrice=1499.40", p -> p.getName().toLowerCase().contains("rower"));
    }
}
