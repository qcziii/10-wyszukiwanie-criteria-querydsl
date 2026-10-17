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
@DisplayName("Scenariusz 04: Po zmianie nazwy pola wyszukiwarka zwraca 500")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("Filtr ceny (cenaOd=1000&cenaDo=2000)")
    void filtrCeny() {
        wynikiZgodne("/api/produkty/szukaj?cenaOd=1000&cenaDo=2000",
                p -> p.getCena().compareTo(new BigDecimal("1000")) >= 0 && p.getCena().compareTo(new BigDecimal("2000")) <= 0);
    }

    @Test
    @DisplayName("Sortowanie po cenie malejąco (sort=cena,desc)")
    void sortowaniePoCenie() {
        Odpowiedz odpowiedz = get("/api/produkty/szukaj?sort=cena,desc&size=100");
        assertThat(odpowiedz.ok()).as("GET /api/produkty/szukaj?sort=cena,desc zwraca %s", odpowiedz.opis()).isTrue();
        List<BigDecimal> ceny = strona(odpowiedz).content().stream().map(ProduktDto::cena).toList();
        assertThat(ceny).as("ceny po sortowaniu malejąco").hasSize(60).isSortedAccordingTo((a, b) -> b.compareTo(a));
    }

    @Test
    @DisplayName("Pozostałe filtry działają (nazwa, kategoria, tagi, ocena)")
    void pozostaleFiltry() {
        wynikiZgodne("/api/produkty/szukaj?nazwa=kabel&kategoria=akcesoria",
                p -> p.getNazwa().toLowerCase().contains("kabel") && p.getKategoria() != null
                        && p.getKategoria().getNazwa().equals("Akcesoria"));
        wynikiZgodne("/api/produkty/szukaj?tagi=eko&ocenaMin=4",
                p -> p.getTagi().stream().anyMatch(t -> t.getNazwa().equals("eko"))
                        && p.getOpinie().stream().anyMatch(o -> o.getOcena() >= 4));
    }
}
