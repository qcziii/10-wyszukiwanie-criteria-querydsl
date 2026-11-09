package pl.kurs.shop.search;

import java.util.Locale;

/** Helper functions for building LIKE patterns. */
final class LikePatterns {

    /** Escape character passed to LIKE ... ESCAPE '\'. */
    static final char ESCAPE = '\\';

    private LikePatterns() {
    }

    /**
     * A "contains text" pattern, in lower case. LIKE special characters typed by the user
     * ({@code %} and {@code _}) are escaped, so they stand for themselves.
     */
    static String contains(String text) {
        String escaped = text.toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + escaped + "%";
    }

    static String lowerCase(String text) {
        return text.toLowerCase(Locale.ROOT);
    }
}
