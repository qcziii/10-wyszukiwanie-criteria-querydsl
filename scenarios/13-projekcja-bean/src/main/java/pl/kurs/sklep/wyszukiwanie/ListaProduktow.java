package pl.kurs.sklep.wyszukiwanie;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.sklep.model.QKategoria;
import pl.kurs.sklep.model.QProdukt;

import java.util.List;

/** Lista podpowiedzi: od razu DTO z bazy (projekcja), bez ładowania encji. */
@Repository
@Transactional(readOnly = true)
public class ListaProduktow {

    private static final QProdukt produkt = QProdukt.produkt;
    private static final QKategoria kategoria = QKategoria.kategoria;

    private final JPAQueryFactory queryFactory;

    public ListaProduktow(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<ProduktSkrot> wszystkie() {
        return queryFactory
                .select(Projections.bean(ProduktSkrot.class,
                        produkt.id, produkt.nazwa, produkt.cena, kategoria.nazwa))
                .from(produkt)
                .leftJoin(produkt.kategoria, kategoria)
                .orderBy(produkt.id.asc())
                .fetch();
    }
}
