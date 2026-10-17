package pl.kurs.sklep;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.kurs.sklep.model.Produkt;
import pl.kurs.sklep.model.Tag;
import pl.kurs.sklep.wyszukiwanie.ProduktDto;
import pl.kurs.sklep.wyszukiwanie.Strona;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/** Testy sprawdzające scenariusz. Uruchamia je check.sh. Nie zmieniaj tego pliku. */
@DisplayName("Scenariusz 11: Produkty bez kategorii znikają z wyników QueryDSL")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("QueryDSL: bez filtrów są wszystkie 60 produktów, też te bez kategorii")
    void wszystkieProdukty() {
        Strona<ProduktDto> strona = wynikiZgodne("/api/produkty/szukaj-qdsl", p -> true);
        assertThat(strona.content()).extracting(ProduktDto::nazwa).as("produkty bez kategorii")
                .contains("Bon rabatowy 100%", "Router Wi-Fi 6");
    }

    @Test
    @DisplayName("QueryDSL: liczba produktów na stronach zgadza się z totalElements (size=10, strona 6)")
    void ostatniaStrona() {
        Odpowiedz odpowiedz = get("/api/produkty/szukaj-qdsl?size=10&page=5");
        assertThat(odpowiedz.ok()).as(odpowiedz.opis()).isTrue();
        Strona<ProduktDto> strona = strona(odpowiedz);
        assertThat(strona.totalElements()).as("totalElements").isEqualTo(60);
        assertThat(idki(strona)).as("produkty na stronie 6 z 6").containsExactly(51L, 52L, 53L, 54L, 55L, 56L, 57L, 58L, 59L, 60L);
    }

    @Test
    @DisplayName("QueryDSL: filtry i sortowanie po kategorii (cenaOd=500&sort=kategoria)")
    void zFiltrem() {
        Strona<ProduktDto> strona = wynikiZgodne("/api/produkty/szukaj-qdsl?sort=kategoria&cenaOd=500",
                p -> p.getCena().compareTo(new BigDecimal("500")) >= 0);
        List<String> kategorie = strona.content().stream().map(ProduktDto::kategoria).filter(k -> k != null).toList();
        assertThat(kategorie).as("nazwy kategorii po kolei").isSorted();
    }

    @Test
    @DisplayName("QueryDSL: filtr kategorii nadal działa (kategoria=sport)")
    void filtrKategorii() {
        wynikiZgodne("/api/produkty/szukaj-qdsl?kategoria=sport",
                p -> p.getKategoria() != null && p.getKategoria().getNazwa().equals("Sport"));
    }
}
