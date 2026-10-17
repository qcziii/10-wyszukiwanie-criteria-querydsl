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
@DisplayName("Scenariusz 09: Po dodaniu metamodelu Hibernate kompilacja nie widzi QProdukt")
class CheckTest extends TestWyszukiwarki {

    @Test
    @DisplayName("Obie wyszukiwarki działają i zwracają to samo")
    void obieWyszukiwarki() {
        for (String adres : List.of("/api/produkty/szukaj", "/api/produkty/szukaj-qdsl")) {
            wynikiZgodne(adres + "?nazwa=rower", p -> p.getNazwa().toLowerCase().contains("rower"));
            wynikiZgodne(adres + "?tagi=eko&cenaDo=100",
                    p -> p.getTagi().stream().anyMatch(t -> t.getNazwa().equals("eko")) && p.getCena().compareTo(new BigDecimal("100")) <= 0);
        }
    }
}
