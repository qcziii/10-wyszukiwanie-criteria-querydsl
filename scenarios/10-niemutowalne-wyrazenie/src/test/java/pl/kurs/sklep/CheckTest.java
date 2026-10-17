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
@DisplayName("Scenariusz 10: Filtr ceny w wersji QueryDSL jest ignorowany")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("QueryDSL: zakres cen (cenaOd=1000&cenaDo=2000)")
    void zakresCen() {
        wynikiZgodne("/api/produkty/szukaj-qdsl?cenaOd=1000&cenaDo=2000",
                p -> p.getCena().compareTo(new BigDecimal("1000")) >= 0 && p.getCena().compareTo(new BigDecimal("2000")) <= 0);
    }

    @Test
    @DisplayName("QueryDSL: cena razem z nazwą (nazwa=kabel&cenaDo=40)")
    void cenaINazwa() {
        wynikiZgodne("/api/produkty/szukaj-qdsl?nazwa=kabel&cenaDo=40",
                p -> p.getNazwa().toLowerCase().contains("kabel") && p.getCena().compareTo(new BigDecimal("40")) <= 0);
    }

    @Test
    @DisplayName("QueryDSL: pozostałe filtry (kategoria, daty, tagi)")
    void pozostale() {
        wynikiZgodne("/api/produkty/szukaj-qdsl?kategoria=sport&dodanyOd=2026-04-05&tagi=promocja",
                p -> p.getKategoria() != null && p.getKategoria().getNazwa().equals("Sport")
                        && !p.getDodany().isBefore(LocalDateTime.of(2026, 4, 5, 0, 0))
                        && p.getTagi().stream().anyMatch(t -> t.getNazwa().equals("promocja")));
    }
}
