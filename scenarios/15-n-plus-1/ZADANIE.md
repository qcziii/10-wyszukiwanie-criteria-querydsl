# 15. Jedno wyszukiwanie wysyła do bazy kilkanaście zapytań

Poziom: **średni** · API: **QueryDSL**

## Uruchomienie

```bash
cd scenarios/15-n-plus-1
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/15-n-plus-1/check.sh
```

## Zgłoszenie

Monitoring bazy pokazuje, że jedno wyszukiwanie w wersji QueryDSL wysyła kilkanaście zapytań SQL (wersja Criteria: 3). Wyniki są poprawne. Ktoś niedawno „uprościł” zapytanie.

```bash
curl "http://localhost:8080/api/produkty/szukaj-qdsl?size=20"
# w logu aplikacji: linie org.hibernate.SQL i podsumowanie org.hibernate.session.metrics („preparing N JDBC statements”)
```

**Oczekiwane:** Jedna strona wyników to najwyżej 3 zapytania SQL (strona, tagi, count), wyniki bez zmian, filtr i sortowanie po kategorii działają.

**Pytanie dodatkowe:** Jakie są inne sposoby na N+1 przy `@ManyToOne` (podpowiedź: `@BatchSize`, `hibernate.default_batch_fetch_size`, projekcja do DTO)? Kiedy który?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
