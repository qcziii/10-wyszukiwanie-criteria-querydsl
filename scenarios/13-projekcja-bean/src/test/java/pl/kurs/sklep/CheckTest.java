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
@DisplayName("Scenariusz 13: Na liście podpowiedzi nazwy produktów to nazwy kategorii")
class CheckTest extends TestWyszukiwarki {

    record Skrot(Long id, String nazwa, BigDecimal cena, String kategoria) {
    }

    private List<Skrot> lista() {
        Odpowiedz odpowiedz = get("/api/produkty/lista");
        assertThat(odpowiedz.ok()).as("GET /api/produkty/lista zwraca %s", odpowiedz.opis()).isTrue();
        return lista(odpowiedz, Skrot.class);
    }

    @Test
    @DisplayName("Lista: nazwy produktów są nazwami produktów")
    void nazwy() {
        assertThat(lista()).extracting(Skrot::nazwa).as("nazwy na liście")
                .containsExactlyElementsOf(zKazdegoProduktu(Produkt::getNazwa));
    }

    @Test
    @DisplayName("Lista: kategorie są wypełnione (null tylko dla produktów bez kategorii)")
    void kategorie() {
        assertThat(lista()).extracting(Skrot::kategoria).as("kategorie na liście")
                .containsExactlyElementsOf(zKazdegoProduktu(p -> p.getKategoria() == null ? null : p.getKategoria().getNazwa()));
    }

    @Test
    @DisplayName("Lista: id i ceny")
    void idICeny() {
        List<Skrot> lista = lista();
        assertThat(lista).extracting(Skrot::id).as("id").containsExactlyElementsOf(zKazdegoProduktu(Produkt::getId));
        assertThat(lista).extracting(Skrot::cena).as("ceny").usingElementComparator(BigDecimal::compareTo)
                .containsExactlyElementsOf(zKazdegoProduktu(Produkt::getCena));
    }
}
