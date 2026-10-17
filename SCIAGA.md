# Ściąga: JPA Criteria API i QueryDSL obok siebie

Przykłady na encjach z tego repo: `Produkt` (z `kategoria`, `producent`, `tagi`, `opinie`), `Kategoria`, `Tag`, `Opinia`.

## Przygotowanie

| | Criteria API | QueryDSL |
|---|---|---|
| punkt wejścia | `CriteriaBuilder cb = em.getCriteriaBuilder();` | `JPAQueryFactory qf = new JPAQueryFactory(em);` (bean) |
| zapytanie | `CriteriaQuery<Produkt> q = cb.createQuery(Produkt.class);` | `qf.selectFrom(produkt)` |
| korzeń | `Root<Produkt> produkt = q.from(Produkt.class);` | `QProdukt produkt = QProdukt.produkt;` |
| klasy pomocnicze | metamodel `Produkt_` (`hibernate-processor`) | `QProdukt` (`querydsl-apt`, klasyfikator `jakarta`) |
| wykonanie | `em.createQuery(q).getResultList()` | `.fetch()`, `.fetchOne()`, `.fetchFirst()` |

## Warunki

| | Criteria API | QueryDSL |
|---|---|---|
| równe | `cb.equal(produkt.get(Produkt_.nazwa), x)` | `produkt.nazwa.eq(x)` |
| zakres | `cb.greaterThanOrEqualTo(produkt.get(Produkt_.cena), od)` | `produkt.cena.goe(od)`, `.loe(do)`, `.between(od, do)` |
| like bez wielkości liter | `cb.like(cb.lower(produkt.get(Produkt_.nazwa)), "%x%", '\\')` | `produkt.nazwa.lower().like("%x%", '\\')`, `containsIgnoreCase(x)` |
| null | `cb.isNull(produkt.get(Produkt_.kategoria))` | `produkt.kategoria.isNull()` |
| in | `produkt.get(Produkt_.id).in(lista)` | `produkt.id.in(lista)` |
| AND / OR | `cb.and(p1, p2)`, `cb.or(p1, p2)` | `p1.and(p2)`, `p1.or(p2)` |
| zbieranie warunków | `List<Predicate>` i `q.where(lista.toArray(Predicate[]::new))` | `BooleanBuilder b = new BooleanBuilder(); b.and(...)` |

## Złączenia

| | Criteria API | QueryDSL |
|---|---|---|
| złączenie wewnętrzne | `produkt.join(Produkt_.kategoria)` (domyślnie INNER) | `.join(produkt.kategoria, kategoria)` |
| złączenie zewnętrzne | `produkt.join(Produkt_.kategoria, JoinType.LEFT)` | `.leftJoin(produkt.kategoria, kategoria)` |
| pobranie od razu (fetch) | `produkt.fetch(Produkt_.kategoria, JoinType.LEFT)` | `.leftJoin(produkt.kategoria, kategoria).fetchJoin()` |
| ścieżka bez złączenia | `produkt.get(Produkt_.kategoria).get(Kategoria_.nazwa)` | `produkt.kategoria.nazwa` |

## Podzapytania

```java
// Criteria: czy produkt ma opinię >= 4
Subquery<Long> sq = q.subquery(Long.class);
Root<Opinia> opinia = sq.from(Opinia.class);
sq.select(opinia.get(Opinia_.id))
  .where(cb.equal(opinia.get(Opinia_.produkt), produkt), cb.ge(opinia.get(Opinia_.ocena), 4));
cb.exists(sq);

// Criteria: podzapytanie po kolekcji produktu z zapytania głównego
Subquery<Long> zTagiem = q.subquery(Long.class);
Root<Produkt> p = zTagiem.correlate(produkt);
Join<Produkt, Tag> tag = p.join(Produkt_.tagi);

// QueryDSL
JPAExpressions.selectOne().from(opinia).where(opinia.produkt.eq(produkt), opinia.ocena.goe(4)).exists();
produkt.tagi.any().nazwa.eq("eko");                  // EXISTS po kolekcji
```

## Projekcje

| | Criteria API | QueryDSL |
|---|---|---|
| konstruktor / rekord | `cb.construct(Dto.class, a, b)` | `Projections.constructor(Dto.class, a, b)` (po kolejności i typach) |
| JavaBean | (brak) | `Projections.bean(Dto.class, a, b.as("pole"))` (po nazwach, przez settery) |
| krotka | `cb.createTupleQuery()` | `qf.select(a, b)` zwraca `Tuple` |

## Sortowanie i stronicowanie

| | Criteria API | QueryDSL |
|---|---|---|
| sortowanie | `q.orderBy(cb.asc(x), cb.desc(y))` | `.orderBy(x.asc(), y.desc())` |
| stronicowanie | `typedQuery.setFirstResult(offset).setMaxResults(size)` | `.offset(offset).limit(size)` |
| liczba wyników | osobne `CriteriaQuery<Long>` z `cb.count(root)` | osobne `qf.select(produkt.count()).from(produkt)...fetchOne()` |

Sortowanie z `Pageable` (`pageable.getSort()`) zamienia się na `Order` / `OrderSpecifier`. Na końcu dobrze dodać sortowanie po `id`, żeby kolejność na kolejnych stronach była stała.

## Spring Data JPA

- `Specification<Produkt>` + `JpaSpecificationExecutor`: `findAll(spec, pageable)`, stronę i zapytanie count robi Spring Data.
- `QuerydslPredicateExecutor<Produkt>`: `findAll(Predicate, Pageable)`.
- `@QuerydslPredicate(root = Produkt.class) Predicate p` w kontrolerze: warunek QueryDSL zbudowany z parametrów adresu.

## Konfiguracja Mavena (QueryDSL 7 z forkiem OpenFeign)

```xml
<properties>
    <!-- Spring Boot zarządza starym com.querydsl przez właściwość querydsl.version, więc tu inna nazwa -->
    <openfeign-querydsl.version>7.7</openfeign-querydsl.version>
</properties>
<dependency>
    <groupId>io.github.openfeign.querydsl</groupId>
    <artifactId>querydsl-jpa</artifactId>
    <version>${openfeign-querydsl.version}</version>
</dependency>
```

Klasy `QProdukt` generuje procesor adnotacji `querydsl-apt` (klasyfikator `jakarta`), a metamodel `Produkt_` procesor `hibernate-processor`; oba uruchamia `maven-compiler-plugin` podczas kompilacji. Fork OpenFeign zachowuje pakiety `com.querydsl.*`, więc kod i Spring Data działają jak ze starym QueryDSL 5.
