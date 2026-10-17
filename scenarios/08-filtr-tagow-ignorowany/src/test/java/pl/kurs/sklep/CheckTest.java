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
@DisplayName("Scenariusz 08: Filtr po tagu zwraca wszystkie produkty")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("Jeden tag (tagi=outlet)")
    void jedenTag() {
        wynikiZgodne("/api/produkty/szukaj?tagi=outlet", p -> p.getTagi().stream().anyMatch(t -> t.getNazwa().equals("outlet")));
    }

    @Test
    @DisplayName("Dwa tagi naraz (tagi=eko,promocja)")
    void dwaTagi() {
        wynikiZgodne("/api/produkty/szukaj?tagi=eko,promocja",
                p -> p.getTagi().stream().map(Tag::getNazwa).collect(Collectors.toSet()).containsAll(Set.of("eko", "promocja")));
    }

    @Test
    @DisplayName("Tag razem z kategorią (tagi=eko&kategoria=dom)")
    void tagIKategoria() {
        wynikiZgodne("/api/produkty/szukaj?tagi=eko&kategoria=dom",
                p -> p.getTagi().stream().anyMatch(t -> t.getNazwa().equals("eko"))
                        && p.getKategoria() != null && p.getKategoria().getNazwa().equals("Dom"));
    }

    @Test
    @DisplayName("Nieistniejący tag nie znajduje nic (tagi=xyz)")
    void nieistniejacyTag() {
        wynikiZgodne("/api/produkty/szukaj?tagi=xyz", p -> false);
    }
}
