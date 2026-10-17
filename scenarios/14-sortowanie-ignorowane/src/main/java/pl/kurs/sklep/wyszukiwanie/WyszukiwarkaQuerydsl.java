package pl.kurs.sklep.wyszukiwanie;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.kurs.sklep.model.Produkt;
import pl.kurs.sklep.model.QKategoria;
import pl.kurs.sklep.model.QOpinia;
import pl.kurs.sklep.model.QProducent;
import pl.kurs.sklep.model.QProdukt;

import java.util.List;

/** Wyszukiwarka produktów na QueryDSL (klasy QProdukt, QKategoria, ... generuje querydsl-apt). */
@Repository
@Transactional(readOnly = true)
public class WyszukiwarkaQuerydsl {

    private static final QProdukt produkt = QProdukt.produkt;
    private static final QKategoria kategoria = QKategoria.kategoria;
    private static final QProducent producent = QProducent.producent;
    private static final QOpinia opinia = QOpinia.opinia;

    private final JPAQueryFactory queryFactory;

    public WyszukiwarkaQuerydsl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Strona<ProduktDto> szukaj(KryteriaWyszukiwania k, Pageable pageable) {
        BooleanBuilder warunki = warunki(k);

        // 1. Strona wyników. Kategorię i producenta pobieramy od razu (fetchJoin), żeby nie było N+1.
        List<Produkt> produkty = queryFactory
                .selectFrom(produkt)
                .leftJoin(produkt.kategoria, kategoria).fetchJoin()
                .leftJoin(produkt.producent, producent).fetchJoin()
                .where(warunki)
                .orderBy(produkt.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<ProduktDto> wyniki = produkty.stream().map(ProduktDto::z).toList();

        // 2. Liczba wszystkich wyników: osobne zapytanie z tymi samymi warunkami.
        Long total = queryFactory
                .select(produkt.count())
                .from(produkt)
                .leftJoin(produkt.kategoria, kategoria)
                .where(warunki)
                .fetchOne();

        return Strona.z(wyniki, total == null ? 0 : total, pageable);
    }

    private BooleanBuilder warunki(KryteriaWyszukiwania k) {
        BooleanBuilder warunki = new BooleanBuilder();
        if (StringUtils.hasText(k.nazwa())) {
            warunki.and(produkt.nazwa.lower().like(Wzorce.zawiera(k.nazwa()), Wzorce.ESCAPE));
        }
        if (StringUtils.hasText(k.kategoria())) {
            warunki.and(kategoria.nazwa.lower().eq(Wzorce.male(k.kategoria())));
        }
        if (k.cenaOd() != null) {
            warunki.and(produkt.cena.goe(k.cenaOd()));
        }
        if (k.cenaDo() != null) {
            warunki.and(produkt.cena.loe(k.cenaDo()));
        }
        if (k.dodanyOd() != null) {
            warunki.and(produkt.dodany.goe(k.dodanyOd().atStartOfDay()));
        }
        if (k.dodanyDo() != null) {
            warunki.and(produkt.dodany.lt(k.dodanyDo().plusDays(1).atStartOfDay()));
        }
        if (k.ocenaMin() != null) {
            warunki.and(JPAExpressions.selectOne().from(opinia)
                    .where(opinia.produkt.eq(produkt), opinia.ocena.goe(k.ocenaMin()))
                    .exists());
        }
        if (k.tagi() != null) {
            // produkt musi mieć KAŻDY z tagów; any() to podzapytanie EXISTS po kolekcji
            for (String nazwaTagu : k.tagi()) {
                if (!StringUtils.hasText(nazwaTagu)) {
                    continue;
                }
                warunki.and(produkt.tagi.any().nazwa.lower().eq(Wzorce.male(nazwaTagu)));
            }
        }
        return warunki;
    }
}
