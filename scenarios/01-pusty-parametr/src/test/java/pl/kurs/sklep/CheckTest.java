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
@DisplayName("Scenariusz 01: Z pustym polem kategoria wyszukiwarka nic nie zwraca")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("Pusty parametr kategoria nie filtruje (kategoria=&cenaDo=100)")
    void pustaKategoria() {
        wynikiZgodne("/api/produkty/szukaj?nazwa=&kategoria=&cenaDo=100",
                p -> p.getCena().compareTo(new BigDecimal("100")) <= 0);
    }

    @Test
    @DisplayName("Kategoria z samych spacji też nie filtruje")
    void kategoriaZeSpacji() {
        wynikiZgodne("/api/produkty/szukaj?kategoria=%20%20&cenaOd=2000", p -> p.getCena().compareTo(new BigDecimal("2000")) >= 0);
    }

    @Test
    @DisplayName("Filtr kategorii nadal działa (kategoria=ogród)")
    void filtrKategorii() {
        wynikiZgodne("/api/produkty/szukaj?kategoria=ogród",
                p -> p.getKategoria() != null && p.getKategoria().getNazwa().equals("Ogród"));
    }
}
