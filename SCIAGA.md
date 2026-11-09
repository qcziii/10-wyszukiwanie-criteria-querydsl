# Ściąga: JPA Criteria API i QueryDSL obok siebie

Przykłady na encjach z tego repo: `Product` (z `category`, `manufacturer`, `tags`, `reviews`), `Category`, `Tag`, `Review`.

## Przygotowanie

| | Criteria API | QueryDSL |
|---|---|---|
| punkt wejścia | `CriteriaBuilder cb = em.getCriteriaBuilder();` | `JPAQueryFactory qf = new JPAQueryFactory(em);` (bean) |
| zapytanie | `CriteriaQuery<Product> q = cb.createQuery(Product.class);` | `qf.selectFrom(product)` |
| korzeń | `Root<Product> product = q.from(Product.class);` | `QProduct product = QProduct.product;` |
| klasy pomocnicze | metamodel `Product_` (`hibernate-processor`) | `QProduct` (`querydsl-apt`, klasyfikator `jakarta`) |
| wykonanie | `em.createQuery(q).getResultList()` | `.fetch()`, `.fetchOne()`, `.fetchFirst()` |

## Warunki

| | Criteria API | QueryDSL |
|---|---|---|
| równe | `cb.equal(product.get(Product_.name), x)` | `product.name.eq(x)` |
| zakres | `cb.greaterThanOrEqualTo(product.get(Product_.price), od)` | `product.price.goe(od)`, `.loe(do)`, `.between(od, do)` |
| like bez wielkości liter | `cb.like(cb.lower(product.get(Product_.name)), "%x%", '\\')` | `product.name.lower().like("%x%", '\\')`, `containsIgnoreCase(x)` |
| null | `cb.isNull(product.get(Product_.category))` | `product.category.isNull()` |
| in | `product.get(Product_.id).in(list)` | `product.id.in(list)` |
| AND / OR | `cb.and(p1, p2)`, `cb.or(p1, p2)` | `p1.and(p2)`, `p1.or(p2)` |
| zbieranie warunków | `List<Predicate>` i `q.where(list.toArray(Predicate[]::new))` | `BooleanBuilder b = new BooleanBuilder(); b.and(...)` |

## Złączenia

| | Criteria API | QueryDSL |
|---|---|---|
| złączenie wewnętrzne | `product.join(Product_.category)` (domyślnie INNER) | `.join(product.category, category)` |
| złączenie zewnętrzne | `product.join(Product_.category, JoinType.LEFT)` | `.leftJoin(product.category, category)` |
| pobranie od razu (fetch) | `product.fetch(Product_.category, JoinType.LEFT)` | `.leftJoin(product.category, category).fetchJoin()` |
| ścieżka bez złączenia | `product.get(Product_.category).get(Category_.name)` | `product.category.name` |

## Podzapytania

```java
// Criteria: czy produkt ma opinię >= 4
Subquery<Long> sq = q.subquery(Long.class);
Root<Review> review = sq.from(Review.class);
sq.select(review.get(Review_.id))
  .where(cb.equal(review.get(Review_.product), product), cb.ge(review.get(Review_.rating), 4));
cb.exists(sq);

// Criteria: podzapytanie po kolekcji produktu z zapytania głównego
Subquery<Long> withTag = q.subquery(Long.class);
Root<Product> p = withTag.correlate(product);
Join<Product, Tag> tag = p.join(Product_.tags);

// QueryDSL
JPAExpressions.selectOne().from(review).where(review.product.eq(product), review.rating.goe(4)).exists();
product.tags.any().name.eq("eko");                  // EXISTS po kolekcji
```

## Projekcje

| | Criteria API | QueryDSL |
|---|---|---|
| konstruktor / rekord | `cb.construct(Dto.class, a, b)` | `Projections.constructor(Dto.class, a, b)` (po kolejności i typach) |
| JavaBean | (brak) | `Projections.bean(Dto.class, a, b.as("field"))` (po nazwach, przez settery) |
| krotka | `cb.createTupleQuery()` | `qf.select(a, b)` zwraca `Tuple` |

## Sortowanie i stronicowanie

| | Criteria API | QueryDSL |
|---|---|---|
| sortowanie | `q.orderBy(cb.asc(x), cb.desc(y))` | `.orderBy(x.asc(), y.desc())` |
| stronicowanie | `typedQuery.setFirstResult(offset).setMaxResults(size)` | `.offset(offset).limit(size)` |
| liczba wyników | osobne `CriteriaQuery<Long>` z `cb.count(root)` | osobne `qf.select(product.count()).from(product)...fetchOne()` |

Sortowanie z `Pageable` (`pageable.getSort()`) zamienia się na `Order` / `OrderSpecifier`. Na końcu dobrze dodać sortowanie po `id`, żeby kolejność na kolejnych stronach była stała.

## Spring Data JPA

- `Specification<Product>` + `JpaSpecificationExecutor`: `findAll(spec, pageable)`, stronę i zapytanie count robi Spring Data.
- `QuerydslPredicateExecutor<Product>`: `findAll(Predicate, Pageable)`.
- `@QuerydslPredicate(root = Product.class) Predicate p` w kontrolerze: warunek QueryDSL zbudowany z parametrów adresu.

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

Klasy `QProduct` generuje procesor adnotacji `querydsl-apt` (klasyfikator `jakarta`), a metamodel `Product_` procesor `hibernate-processor`; oba uruchamia `maven-compiler-plugin` podczas kompilacji. Fork OpenFeign zachowuje pakiety `com.querydsl.*`, więc kod i Spring Data działają jak ze starym QueryDSL 5.
