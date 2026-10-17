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
@DisplayName("Scenariusz 14: Parametr sort w wersji QueryDSL nic nie zmienia")
class CheckTest extends TestWyszukiwarki {

    private List<Long> kolejnosc(String adres) {
        Odpowiedz odpowiedz = get(adres);
        assertThat(odpowiedz.ok()).as("GET %s zwraca %s", adres, odpowiedz.opis()).isTrue();
        return idki(strona(odpowiedz));
    }

    @Test
    @DisplayName("QueryDSL: sort=cena,desc daje ceny malejąco")
    void cenaMalejaco() {
        Odpowiedz odpowiedz = get("/api/produkty/szukaj-qdsl?sort=cena,desc&size=100");
        assertThat(odpowiedz.ok()).as(odpowiedz.opis()).isTrue();
        assertThat(strona(odpowiedz).content()).extracting(ProduktDto::cena).as("ceny")
                .hasSize(60).isSortedAccordingTo((a, b) -> b.compareTo(a));
    }

    @Test
    @DisplayName("QueryDSL: ta sama kolejność co w wersji Criteria (nazwa, dodany, kategoria, kilka pól)")
    void jakCriteria() {
        for (String sort : List.of("nazwa", "dodany,desc", "kategoria", "cena&sort=nazwa,desc")) {
            String parametry = "?size=100&sort=" + sort;
            assertThat(kolejnosc("/api/produkty/szukaj-qdsl" + parametry)).as("kolejność id dla sort=%s", sort)
                    .isEqualTo(kolejnosc("/api/produkty/szukaj" + parametry));
        }
    }

    @Test
    @DisplayName("QueryDSL: druga strona przy sort=cena (size=7&page=1)")
    void drugaStrona() {
        String parametry = "?sort=cena&size=7&page=1";
        assertThat(kolejnosc("/api/produkty/szukaj-qdsl" + parametry)).as("produkty na drugiej stronie")
                .isEqualTo(kolejnosc("/api/produkty/szukaj" + parametry));
    }

    @Test
    @DisplayName("QueryDSL: sort po polu spoza listy (sort=cenaZakupu) to błąd 400")
    void niedozwolonePole() {
        Odpowiedz odpowiedz = get("/api/produkty/szukaj-qdsl?sort=cenaZakupu");
        assertThat(odpowiedz.status()).as("status dla sort=cenaZakupu (%s)", odpowiedz.opis()).isEqualTo(400);
    }
}
