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
@DisplayName("Scenariusz 03: Szukanie laptop nie znajduje Laptop Dell, a 100% znajduje za dużo")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("Fragment nazwy bez względu na wielkość liter (nazwa=laptop)")
    void fragmentNazwy() {
        wynikiZgodne("/api/produkty/szukaj?nazwa=laptop", p -> p.getNazwa().toLowerCase().contains("laptop"));
    }

    @Test
    @DisplayName("Polskie znaki wielkimi literami (nazwa=ŁÓDKA)")
    void polskieZnaki() {
        wynikiZgodne("/api/produkty/szukaj?nazwa=%C5%81%C3%93DKA", p -> p.getNazwa().toLowerCase().contains("łódka"));
    }

    @Test
    @DisplayName("Znak % w szukanym tekście oznacza procent (nazwa=100%)")
    void procent() {
        wynikiZgodne("/api/produkty/szukaj?nazwa=100%25", p -> p.getNazwa().contains("100%"));
    }

    @Test
    @DisplayName("Znak _ w szukanym tekście oznacza podkreślnik (nazwa=kabel_usb)")
    void podkreslnik() {
        wynikiZgodne("/api/produkty/szukaj?nazwa=kabel_usb", p -> p.getNazwa().toLowerCase().contains("kabel_usb"));
    }
}
