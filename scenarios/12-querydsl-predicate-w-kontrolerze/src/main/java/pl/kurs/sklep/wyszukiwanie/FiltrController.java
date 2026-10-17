package pl.kurs.sklep.wyszukiwanie;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.sklep.model.Produkt;

/**
 * Filtr dla panelu administracyjnego: parametry adresu to nazwy pól encji,
 * np. {@code /api/produkty/filtr?nazwa=lapt&kategoria.nazwa=dom}.
 */
@RestController
class FiltrController {

    private final FiltrProduktow filtr;

    FiltrController(FiltrProduktow filtr) {
        this.filtr = filtr;
    }

    @GetMapping("/api/produkty/filtr")
    Strona<ProduktDto> filtr(@QuerydslPredicate(root = Produkt.class) Predicate warunek, Pageable pageable) {
        return filtr.filtruj(warunek, pageable);
    }
}
