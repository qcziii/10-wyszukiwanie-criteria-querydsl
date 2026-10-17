package pl.kurs.sklep.wyszukiwanie;

import java.util.Locale;

/** Pomocnicze funkcje do budowania wzorców LIKE. */
final class Wzorce {

    /** Znak ucieczki przekazywany do LIKE ... ESCAPE '\'. */
    static final char ESCAPE = '\\';

    private Wzorce() {
    }

    /**
     * Wzorzec „zawiera tekst”, małymi literami. Znaki specjalne LIKE wpisane przez użytkownika
     * ({@code %} i {@code _}) są poprzedzone znakiem ucieczki, więc oznaczają same siebie.
     */
    static String zawiera(String tekst) {
        String bezpieczny = tekst.toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + bezpieczny + "%";
    }

    static String male(String tekst) {
        return tekst.toLowerCase(Locale.ROOT);
    }
}
