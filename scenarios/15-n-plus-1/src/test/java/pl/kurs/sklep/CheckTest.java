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
@DisplayName("Scenariusz 15: Jedno wyszukiwanie wysyła do bazy kilkanaście zapytań")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("QueryDSL: strona 20 produktów to najwyżej 3 zapytania SQL")
    void malozapytan() {
        Odpowiedz odpowiedz = get("/api/produkty/szukaj-qdsl?size=20");
        assertThat(odpowiedz.ok()).as(odpowiedz.opis()).isTrue();
        assertThat(odpowiedz.sql()).as("zapytania SQL dla jednej strony (strona, tagi, count)").hasSizeLessThanOrEqualTo(3);
    }

    @Test
    @DisplayName("QueryDSL: kategoria i producent są w wynikach")
    void kategoriaIProducent() {
        Odpowiedz odpowiedz = get("/api/produkty/szukaj-qdsl?size=100");
        assertThat(odpowiedz.ok()).as(odpowiedz.opis()).isTrue();
        Strona<ProduktDto> strona = strona(odpowiedz);
        assertThat(strona.content()).extracting(ProduktDto::kategoria).as("kategorie")
                .containsExactlyElementsOf(zKazdegoProduktu(p -> p.getKategoria() == null ? null : p.getKategoria().getNazwa()));
        assertThat(strona.content()).extracting(ProduktDto::producent).as("producenci")
                .containsExactlyElementsOf(zKazdegoProduktu(p -> p.getProducent().getNazwa()));
    }

    @Test
    @DisplayName("QueryDSL: filtr i sortowanie po kategorii nadal działają")
    void filtr() {
        wynikiZgodne("/api/produkty/szukaj-qdsl?kategoria=akcesoria&sort=kategoria",
                p -> p.getKategoria() != null && p.getKategoria().getNazwa().equals("Akcesoria"));
    }
}
