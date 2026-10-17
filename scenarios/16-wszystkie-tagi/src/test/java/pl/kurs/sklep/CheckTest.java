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
@DisplayName("Scenariusz 16: Filtr tagi=eko,promocja zwraca produkty z którymkolwiek tagiem")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("Criteria: tagi=eko,promocja to produkty z OBOMA tagami")
    void criteriaDwaTagi() {
        wynikiZgodne("/api/produkty/szukaj?tagi=eko,promocja", p -> p.getTagi().stream().map(Tag::getNazwa).collect(Collectors.toSet()).containsAll(Set.of("eko", "promocja")));
    }

    @Test
    @DisplayName("QueryDSL: tagi=eko,promocja to produkty z OBOMA tagami")
    void querydslDwaTagi() {
        wynikiZgodne("/api/produkty/szukaj-qdsl?tagi=eko,promocja", p -> p.getTagi().stream().map(Tag::getNazwa).collect(Collectors.toSet()).containsAll(Set.of("eko", "promocja")));
    }

    @Test
    @DisplayName("Obie wersje: trzy tagi i wielkość liter (tagi=EKO,promocja,nowosc)")
    void trzyTagi() {
        for (String adres : List.of("/api/produkty/szukaj", "/api/produkty/szukaj-qdsl")) {
            wynikiZgodne(adres + "?tagi=EKO,promocja,nowosc", p -> p.getTagi().stream().map(Tag::getNazwa).collect(Collectors.toSet()).containsAll(Set.of("eko", "promocja", "nowosc")));
        }
    }

    @Test
    @DisplayName("Obie wersje: jeden tag, strona po 5, poprawne totalElements")
    void jedenTagStronicowanie() {
        List<Long> oczekiwane = oczekiwane(p -> p.getTagi().stream().anyMatch(t -> t.getNazwa().equals("promocja")));
        for (String adres : List.of("/api/produkty/szukaj", "/api/produkty/szukaj-qdsl")) {
            Odpowiedz odpowiedz = get(adres + "?tagi=promocja&size=5&page=1");
            assertThat(odpowiedz.ok()).as(odpowiedz.opis()).isTrue();
            Strona<ProduktDto> strona = strona(odpowiedz);
            assertThat(idki(strona)).as("%s: druga strona", adres).isEqualTo(oczekiwane.subList(5, 10));
            assertThat(strona.totalElements()).as("%s: totalElements", adres).isEqualTo(oczekiwane.size());
        }
    }
}
