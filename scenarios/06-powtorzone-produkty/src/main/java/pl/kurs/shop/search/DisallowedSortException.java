package pl.kurs.shop.search;

public class DisallowedSortException extends RuntimeException {

    public DisallowedSortException(String field) {
        super("Nie można sortować po polu '" + field + "'. Dozwolone: name, price, addedAt, category");
    }
}
