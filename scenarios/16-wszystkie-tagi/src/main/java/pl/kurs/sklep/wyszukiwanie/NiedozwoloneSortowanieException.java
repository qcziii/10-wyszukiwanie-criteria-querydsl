package pl.kurs.sklep.wyszukiwanie;

public class NiedozwoloneSortowanieException extends RuntimeException {

    public NiedozwoloneSortowanieException(String pole) {
        super("Nie można sortować po polu '" + pole + "'. Dozwolone: nazwa, cena, dodany, kategoria");
    }
}
