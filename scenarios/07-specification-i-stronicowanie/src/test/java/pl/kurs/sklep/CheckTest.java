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
@DisplayName("Scenariusz 07: Po przejściu na Specification stronicowanie kończy się błędem")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("Pierwsza strona po 5 produktów (size=5)")
    void pierwszaStrona() {
        Odpowiedz odpowiedz = get("/api/produkty/szukaj?size=5");
        assertThat(odpowiedz.ok()).as("GET /api/produkty/szukaj?size=5 zwraca %s", odpowiedz.opis()).isTrue();
        Strona<ProduktDto> strona = strona(odpowiedz);
        assertThat(idki(strona)).as("produkty na pierwszej stronie").containsExactly(1L, 2L, 3L, 4L, 5L);
        assertThat(strona.totalElements()).as("totalElements").isEqualTo(60);
    }

    @Test
    @DisplayName("Druga strona z filtrem (kategoria=dom&size=3&page=1)")
    void drugaStronaZFiltrem() {
        List<Long> oczekiwane = oczekiwane(p -> p.getKategoria() != null && p.getKategoria().getNazwa().equals("Dom"));
        Odpowiedz odpowiedz = get("/api/produkty/szukaj?kategoria=dom&size=3&page=1");
        assertThat(odpowiedz.ok()).as("GET /api/produkty/szukaj?kategoria=dom&size=3&page=1 zwraca %s", odpowiedz.opis()).isTrue();
        Strona<ProduktDto> strona = strona(odpowiedz);
        assertThat(idki(strona)).as("produkty na drugiej stronie").isEqualTo(oczekiwane.subList(3, 6));
        assertThat(strona.totalElements()).as("totalElements").isEqualTo(oczekiwane.size());
    }

    @Test
    @DisplayName("Strona wyników to najwyżej 3 zapytania SQL (bez N+1)")
    void bezNPlusJeden() {
        Odpowiedz odpowiedz = get("/api/produkty/szukaj?size=20");
        assertThat(odpowiedz.ok()).as(odpowiedz.opis()).isTrue();
        assertThat(odpowiedz.sql()).as("zapytania SQL dla jednej strony (strona, tagi, count)").hasSizeLessThanOrEqualTo(3);
    }

    @Test
    @DisplayName("Filtry (nazwa, cena, tagi)")
    void filtry() {
        wynikiZgodne("/api/produkty/szukaj?nazwa=rower&cenaDo=2000",
                p -> p.getNazwa().toLowerCase().contains("rower") && p.getCena().compareTo(new BigDecimal("2000")) <= 0);
        wynikiZgodne("/api/produkty/szukaj?tagi=eko,promocja",
                p -> p.getTagi().stream().map(Tag::getNazwa).collect(Collectors.toSet()).containsAll(Set.of("eko", "promocja")));
    }
}
