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
@DisplayName("Scenariusz 05: Raport dodane do 31.03 nie zawiera produktów z 31.03")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("Marzec: dodanyOd=2026-03-01&dodanyDo=2026-03-31 obejmuje cały 31 marca")
    void marzec() {
        wynikiZgodne("/api/produkty/szukaj?dodanyOd=2026-03-01&dodanyDo=2026-03-31",
                p -> !p.getDodany().isBefore(LocalDateTime.of(2026, 3, 1, 0, 0))
                        && p.getDodany().isBefore(LocalDateTime.of(2026, 4, 1, 0, 0)));
    }

    @Test
    @DisplayName("Jeden dzień: dodanyOd=dodanyDo=2026-03-31")
    void jedenDzien() {
        wynikiZgodne("/api/produkty/szukaj?dodanyOd=2026-03-31&dodanyDo=2026-03-31",
                p -> p.getDodany().toLocalDate().equals(java.time.LocalDate.of(2026, 3, 31)));
    }

    @Test
    @DisplayName("Produkt dodany 1 kwietnia o północy nie należy do marca")
    void polnoc() {
        Odpowiedz odpowiedz = get("/api/produkty/szukaj?dodanyDo=2026-03-31&size=100");
        assertThat(odpowiedz.ok()).as(odpowiedz.opis()).isTrue();
        assertThat(strona(odpowiedz).content()).extracting(ProduktDto::nazwa)
                .as("produkty dodane do 31 marca").doesNotContain("Laptop Samsung Galaxy Book");
    }
}
