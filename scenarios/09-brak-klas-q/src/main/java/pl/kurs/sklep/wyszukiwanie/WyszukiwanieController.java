package pl.kurs.sklep.wyszukiwanie;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ta sama wyszukiwarka w dwóch wersjach. Obie mają zwracać dokładnie te same wyniki.
 * Stronicowanie i sortowanie: {@code ?page=0&size=20&sort=cena,desc}.
 */
@RestController
@RequestMapping("/api/produkty")
class WyszukiwanieController {

    private final WyszukiwarkaCriteria criteria;
    private final WyszukiwarkaQuerydsl querydsl;

    WyszukiwanieController(WyszukiwarkaCriteria criteria, WyszukiwarkaQuerydsl querydsl) {
        this.criteria = criteria;
        this.querydsl = querydsl;
    }

    /** Wersja na JPA Criteria API. */
    @GetMapping("/szukaj")
    Strona<ProduktDto> szukaj(KryteriaWyszukiwania kryteria, Pageable pageable) {
        return criteria.szukaj(kryteria, pageable);
    }

    /** Wersja na QueryDSL. */
    @GetMapping("/szukaj-qdsl")
    Strona<ProduktDto> szukajQuerydsl(KryteriaWyszukiwania kryteria, Pageable pageable) {
        return querydsl.szukaj(kryteria, pageable);
    }
}
