package pl.kurs.sklep.wyszukiwanie;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Parametry wyszukiwarki z adresu, np. {@code ?nazwa=laptop&cenaDo=3000&tagi=eko,promocja}.
 * Każdy parametr jest opcjonalny: brak parametru (albo pusty parametr) oznacza „nie filtruj po tym”.
 *
 * @param nazwa     fragment nazwy, bez rozróżniania wielkości liter
 * @param kategoria dokładna nazwa kategorii, bez rozróżniania wielkości liter
 * @param cenaOd    cena minimalna (włącznie)
 * @param cenaDo    cena maksymalna (włącznie)
 * @param dodanyOd  pierwszy dzień, w którym produkt mógł zostać dodany (włącznie)
 * @param dodanyDo  ostatni dzień, w którym produkt mógł zostać dodany (włącznie, cały dzień)
 * @param ocenaMin  tylko produkty, które mają co najmniej jedną opinię z taką albo wyższą oceną
 * @param tagi      produkt musi mieć <b>wszystkie</b> podane tagi
 */
public record KryteriaWyszukiwania(
        String nazwa,
        String kategoria,
        BigDecimal cenaOd,
        BigDecimal cenaDo,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dodanyOd,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dodanyDo,
        Integer ocenaMin,
        List<String> tagi) {
}
