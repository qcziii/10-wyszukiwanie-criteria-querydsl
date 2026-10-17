package pl.kurs.sklep.wyszukiwanie;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.sklep.model.Produkt;
import pl.kurs.sklep.model.ProduktRepository;

/** Prosty filtr: warunek QueryDSL zbudowany przez Spring Data z parametrów adresu. */
@Service
@Transactional(readOnly = true)
public class FiltrProduktow {

    private final ProduktRepository produkty;

    public FiltrProduktow(ProduktRepository produkty) {
        this.produkty = produkty;
    }

    public Strona<ProduktDto> filtruj(Predicate warunek, Pageable pageable) {
        Page<Produkt> wynik = produkty.findAll(warunek, pageable);
        return Strona.z(wynik.getContent().stream().map(ProduktDto::z).toList(), wynik.getTotalElements(), pageable);
    }
}
