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
@DisplayName("Scenariusz 12: Filtr nie znajduje lapt, za to pozwala filtrować po cenie zakupu")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("Filtr: fragment nazwy bez względu na wielkość liter (nazwa=LAPT)")
    void fragmentNazwy() {
        wynikiZgodne("/api/produkty/filtr?nazwa=LAPT", p -> p.getNazwa().toLowerCase().contains("lapt"));
    }

    @Test
    @DisplayName("Filtr: fragment nazwy kategorii (kategoria.nazwa=ogr)")
    void kategoria() {
        wynikiZgodne("/api/produkty/filtr?kategoria.nazwa=ogr",
                p -> p.getKategoria() != null && p.getKategoria().getNazwa().toLowerCase().contains("ogr"));
    }

    @Test
    @DisplayName("Filtr: dokładna cena (cena=99.99)")
    void cena() {
        wynikiZgodne("/api/produkty/filtr?cena=99.99", p -> p.getCena().compareTo(new BigDecimal("99.99")) == 0);
    }

    @Test
    @DisplayName("Filtr: parametr cenaZakupu jest ignorowany (pole poufne)")
    void cenaZakupu() {
        wynikiZgodne("/api/produkty/filtr?cenaZakupu=0.00", p -> true);
        wynikiZgodne("/api/produkty/filtr?nazwa=rower&cenaZakupu=1499.40", p -> p.getNazwa().toLowerCase().contains("rower"));
    }
}
