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
@DisplayName("Scenariusz 06: Po filtrze po ocenie produkty się powtarzają")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("ocenaMin=4: każdy produkt raz i poprawne totalElements")
    void ocena4() {
        wynikiZgodne("/api/produkty/szukaj?ocenaMin=4", p -> p.getOpinie().stream().anyMatch(o -> o.getOcena() >= 4));
    }

    @Test
    @DisplayName("ocenaMin=4, strona po 10: 10 różnych produktów, właściwa liczba stron")
    void stronicowanie() {
        List<Long> oczekiwane = oczekiwane(p -> p.getOpinie().stream().anyMatch(o -> o.getOcena() >= 4));
        Odpowiedz odpowiedz = get("/api/produkty/szukaj?ocenaMin=4&size=10");
        assertThat(odpowiedz.ok()).as(odpowiedz.opis()).isTrue();
        Strona<ProduktDto> strona = strona(odpowiedz);
        assertThat(idki(strona)).as("pierwsza strona (size=10)").isEqualTo(oczekiwane.subList(0, 10));
        assertThat(strona.totalPages()).as("totalPages").isEqualTo((oczekiwane.size() + 9) / 10);
    }

    @Test
    @DisplayName("ocenaMin=5 razem z nazwą")
    void ocena5() {
        wynikiZgodne("/api/produkty/szukaj?ocenaMin=5&nazwa=a",
                p -> p.getNazwa().toLowerCase().contains("a") && p.getOpinie().stream().anyMatch(o -> o.getOcena() >= 5));
    }
}
