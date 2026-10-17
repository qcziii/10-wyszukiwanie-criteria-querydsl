package pl.kurs.sklep.wyszukiwanie;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import pl.kurs.sklep.model.Kategoria;
import pl.kurs.sklep.model.Kategoria_;
import pl.kurs.sklep.model.Opinia;
import pl.kurs.sklep.model.Opinia_;
import pl.kurs.sklep.model.Produkt;
import pl.kurs.sklep.model.Produkt_;
import pl.kurs.sklep.model.Tag;
import pl.kurs.sklep.model.Tag_;

import java.util.ArrayList;
import java.util.List;

/** Warunki wyszukiwania jako Specification. Spring Data użyje jej i do strony wyników, i do zapytania count. */
final class ProduktSpecyfikacje {

    private ProduktSpecyfikacje() {
    }

    static Specification<Produkt> spelnia(KryteriaWyszukiwania k) {
        return (produkt, query, cb) -> {
            // kategorię i producenta pobieramy od razu (fetch), żeby nie było N+1
            @SuppressWarnings("unchecked")
            Join<Produkt, Kategoria> kategoria = (Join<Produkt, Kategoria>) produkt.fetch(Produkt_.kategoria, JoinType.LEFT);
            produkt.fetch(Produkt_.producent, JoinType.LEFT);
            List<Predicate> warunki = new ArrayList<>();
            if (StringUtils.hasText(k.nazwa())) {
                warunki.add(cb.like(cb.lower(produkt.get(Produkt_.nazwa)), Wzorce.zawiera(k.nazwa()), Wzorce.ESCAPE));
            }
            if (StringUtils.hasText(k.kategoria())) {
                warunki.add(cb.equal(cb.lower(kategoria.get(Kategoria_.nazwa)), Wzorce.male(k.kategoria())));
            }
            if (k.cenaOd() != null) {
                warunki.add(cb.greaterThanOrEqualTo(produkt.get(Produkt_.cena), k.cenaOd()));
            }
            if (k.cenaDo() != null) {
                warunki.add(cb.lessThanOrEqualTo(produkt.get(Produkt_.cena), k.cenaDo()));
            }
            if (k.dodanyOd() != null) {
                warunki.add(cb.greaterThanOrEqualTo(produkt.get(Produkt_.dodany), k.dodanyOd().atStartOfDay()));
            }
            if (k.dodanyDo() != null) {
                // cały ostatni dzień: wszystko przed północą następnego dnia
                warunki.add(cb.lessThan(produkt.get(Produkt_.dodany), k.dodanyDo().plusDays(1).atStartOfDay()));
            }
            if (k.ocenaMin() != null) {
                Subquery<Long> opinie = query.subquery(Long.class);
                Root<Opinia> opinia = opinie.from(Opinia.class);
                opinie.select(opinia.get(Opinia_.id))
                        .where(cb.equal(opinia.get(Opinia_.produkt), produkt),
                                cb.greaterThanOrEqualTo(opinia.get(Opinia_.ocena), k.ocenaMin()));
                warunki.add(cb.exists(opinie));
            }
            if (k.tagi() != null) {
                for (String nazwaTagu : k.tagi()) {
                    if (!StringUtils.hasText(nazwaTagu)) {
                        continue;
                    }
                    Subquery<Long> zTagiem = query.subquery(Long.class);
                    Root<Produkt> p = zTagiem.correlate(produkt);
                    Join<Produkt, Tag> tag = p.join(Produkt_.tagi);
                    zTagiem.select(tag.get(Tag_.id))
                            .where(cb.equal(cb.lower(tag.get(Tag_.nazwa)), Wzorce.male(nazwaTagu)));
                    warunki.add(cb.exists(zTagiem));
                }
            }
            return cb.and(warunki.toArray(Predicate[]::new));
        };
    }
}
