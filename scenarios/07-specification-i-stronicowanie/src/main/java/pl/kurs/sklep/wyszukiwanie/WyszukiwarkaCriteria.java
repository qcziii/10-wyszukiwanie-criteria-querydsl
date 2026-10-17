package pl.kurs.sklep.wyszukiwanie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.sklep.model.Produkt;
import pl.kurs.sklep.model.ProduktRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Wyszukiwarka produktów na JPA Criteria API, przez Specification ze Spring Data JPA.
 * Warunki są w {@link ProduktSpecyfikacje}, stronicowanie i zapytanie count robi Spring Data.
 */
@Repository
@Transactional(readOnly = true)
public class WyszukiwarkaCriteria {

    private final ProduktRepository produkty;

    public WyszukiwarkaCriteria(ProduktRepository produkty) {
        this.produkty = produkty;
    }

    public Strona<ProduktDto> szukaj(KryteriaWyszukiwania k, Pageable pageable) {
        PageRequest zadanie = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                sortowanie(pageable.getSort()));
        Page<Produkt> wynik = produkty.findAll(ProduktSpecyfikacje.spelnia(k), zadanie);
        return Strona.z(wynik.getContent().stream().map(ProduktDto::z).toList(), wynik.getTotalElements(), pageable);
    }

    private static Sort sortowanie(Sort sort) {
        List<Sort.Order> kolejnosc = new ArrayList<>();
        for (Sort.Order o : sort) {
            String pole = switch (o.getProperty()) {
                case "nazwa", "cena", "dodany" -> o.getProperty();
                case "kategoria" -> "kategoria.nazwa";
                default -> throw new NiedozwoloneSortowanieException(o.getProperty());
            };
            kolejnosc.add(new Sort.Order(o.getDirection(), pole));
        }
        // id na końcu: stała kolejność produktów o tej samej cenie/nazwie między stronami
        kolejnosc.add(Sort.Order.asc("id"));
        return Sort.by(kolejnosc);
    }
}
