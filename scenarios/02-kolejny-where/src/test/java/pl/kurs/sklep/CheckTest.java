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
@DisplayName("Scenariusz 02: Z dwoma filtrami działa tylko jeden")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("Nazwa i cena naraz (nazwa=laptop&cenaDo=3000)")
    void nazwaICena() {
        wynikiZgodne("/api/produkty/szukaj?nazwa=laptop&cenaDo=3000",
                p -> p.getNazwa().toLowerCase().contains("laptop") && p.getCena().compareTo(new BigDecimal("3000")) <= 0);
    }

    @Test
    @DisplayName("Kategoria i zakres cen naraz (kategoria=telefony&cenaOd=500&cenaDo=1500)")
    void kategoriaIZakres() {
        wynikiZgodne("/api/produkty/szukaj?kategoria=telefony&cenaOd=500&cenaDo=1500",
                p -> p.getKategoria() != null && p.getKategoria().getNazwa().equals("Telefony")
                        && p.getCena().compareTo(new BigDecimal("500")) >= 0
                        && p.getCena().compareTo(new BigDecimal("1500")) <= 0);
    }

    @Test
    @DisplayName("Pojedynczy filtr nadal działa (cenaDo=50)")
    void jedenFiltr() {
        wynikiZgodne("/api/produkty/szukaj?cenaDo=50", p -> p.getCena().compareTo(new BigDecimal("50")) <= 0);
    }
}
