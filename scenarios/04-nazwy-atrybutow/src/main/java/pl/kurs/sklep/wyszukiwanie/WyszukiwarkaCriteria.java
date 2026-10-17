package pl.kurs.sklep.wyszukiwanie;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.kurs.sklep.model.Kategoria;
import pl.kurs.sklep.model.Opinia;
import pl.kurs.sklep.model.Produkt;
import pl.kurs.sklep.model.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Wyszukiwarka produktów na JPA Criteria API. */
@Repository
@Transactional(readOnly = true)
public class WyszukiwarkaCriteria {

    private final EntityManager em;

    public WyszukiwarkaCriteria(EntityManager em) {
        this.em = em;
    }

    public Strona<ProduktDto> szukaj(KryteriaWyszukiwania k, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 1. Strona wyników. Kategorię i producenta pobieramy od razu (fetch), żeby nie było N+1.
        CriteriaQuery<Produkt> query = cb.createQuery(Produkt.class);
        Root<Produkt> produkt = query.from(Produkt.class);
        Join<Produkt, Kategoria> kategoria = (Join<Produkt, Kategoria>) produkt.<Produkt, Kategoria>fetch("kategoria", JoinType.LEFT);
        produkt.fetch("producent", JoinType.LEFT);
        query.select(produkt)
                .where(warunki(cb, query, produkt, kategoria, k).toArray(Predicate[]::new))
                .orderBy(sortowanie(cb, produkt, kategoria, pageable.getSort()));
        List<ProduktDto> wyniki = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList().stream()
                .map(ProduktDto::z)
                .toList();

        // 2. Liczba wszystkich wyników: osobne zapytanie z tymi samymi warunkami, bez fetch i sortowania.
        CriteriaQuery<Long> count = cb.createQuery(Long.class);
        Root<Produkt> produktCount = count.from(Produkt.class);
        Join<Produkt, Kategoria> kategoriaCount = produktCount.join("kategoria", JoinType.LEFT);
        count.select(cb.count(produktCount))
                .where(warunki(cb, count, produktCount, kategoriaCount, k).toArray(Predicate[]::new));
        long total = em.createQuery(count).getSingleResult();

        return Strona.z(wyniki, total, pageable);
    }

    private List<Predicate> warunki(CriteriaBuilder cb, CriteriaQuery<?> query, Root<Produkt> produkt,
                                    Join<Produkt, Kategoria> kategoria, KryteriaWyszukiwania k) {
        List<Predicate> warunki = new ArrayList<>();
        if (StringUtils.hasText(k.nazwa())) {
            warunki.add(cb.like(cb.lower(produkt.<String>get("nazwa")), Wzorce.zawiera(k.nazwa()), Wzorce.ESCAPE));
        }
        if (StringUtils.hasText(k.kategoria())) {
            warunki.add(cb.equal(cb.lower(kategoria.<String>get("nazwa")), Wzorce.male(k.kategoria())));
        }
        if (k.cenaOd() != null) {
            warunki.add(cb.greaterThanOrEqualTo(produkt.<BigDecimal>get("cenaBrutto"), k.cenaOd()));
        }
        if (k.cenaDo() != null) {
            warunki.add(cb.lessThanOrEqualTo(produkt.<BigDecimal>get("cenaBrutto"), k.cenaDo()));
        }
        if (k.dodanyOd() != null) {
            warunki.add(cb.greaterThanOrEqualTo(produkt.<LocalDateTime>get("dodany"), k.dodanyOd().atStartOfDay()));
        }
        if (k.dodanyDo() != null) {
            // cały ostatni dzień: wszystko przed północą następnego dnia
            warunki.add(cb.lessThan(produkt.<LocalDateTime>get("dodany"), k.dodanyDo().plusDays(1).atStartOfDay()));
        }
        if (k.ocenaMin() != null) {
            // EXISTS zamiast JOIN: produkt z kilkoma pasującymi opiniami nadal jest jednym wierszem
            Subquery<Long> opinie = query.subquery(Long.class);
            Root<Opinia> opinia = opinie.from(Opinia.class);
            opinie.select(opinia.<Long>get("id"))
                    .where(cb.equal(opinia.get("produkt"), produkt),
                            cb.greaterThanOrEqualTo(opinia.<Integer>get("ocena"), k.ocenaMin()));
            warunki.add(cb.exists(opinie));
        }
        if (k.tagi() != null) {
            // produkt musi mieć KAŻDY z tagów: osobne EXISTS dla każdego tagu
            for (String nazwaTagu : k.tagi()) {
                if (!StringUtils.hasText(nazwaTagu)) {
                    continue;
                }
                Subquery<Long> zTagiem = query.subquery(Long.class);
                Root<Produkt> p = zTagiem.correlate(produkt);
                Join<Produkt, Tag> tag = p.join("tagi");
                zTagiem.select(tag.<Long>get("id"))
                        .where(cb.equal(cb.lower(tag.<String>get("nazwa")), Wzorce.male(nazwaTagu)));
                warunki.add(cb.exists(zTagiem));
            }
        }
        return warunki;
    }

    private List<Order> sortowanie(CriteriaBuilder cb, Root<Produkt> produkt,
                                   Join<Produkt, Kategoria> kategoria, Sort sort) {
        List<Order> kolejnosc = new ArrayList<>();
        for (Sort.Order o : sort) {
            Expression<?> pole = switch (o.getProperty()) {
                case "nazwa" -> produkt.get("nazwa");
                case "cena" -> produkt.<BigDecimal>get("cenaBrutto");
                case "dodany" -> produkt.<LocalDateTime>get("dodany");
                case "kategoria" -> kategoria.get("nazwa");
                default -> throw new NiedozwoloneSortowanieException(o.getProperty());
            };
            kolejnosc.add(o.isAscending() ? cb.asc(pole) : cb.desc(pole));
        }
        // id na końcu: stała kolejność produktów o tej samej cenie/nazwie między stronami
        kolejnosc.add(cb.asc(produkt.get("id")));
        return kolejnosc;
    }
}
