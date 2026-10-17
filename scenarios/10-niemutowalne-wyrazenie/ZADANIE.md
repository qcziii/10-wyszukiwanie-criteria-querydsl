# 10. Filtr ceny w wersji QueryDSL jest ignorowany

Poziom: **łatwy** · API: **QueryDSL**

## Uruchomienie

```bash
cd scenarios/10-niemutowalne-wyrazenie
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/10-niemutowalne-wyrazenie/check.sh
```

## Zgłoszenie

Wersja QueryDSL ignoruje filtry ceny: `cenaOd=1000&cenaDo=2000` zwraca wszystko. Wersja Criteria z tymi samymi parametrami działa. Pozostałe filtry w QueryDSL działają.

```bash
curl "http://localhost:8080/api/produkty/szukaj-qdsl?cenaOd=1000&cenaDo=2000"
curl "http://localhost:8080/api/produkty/szukaj?cenaOd=1000&cenaDo=2000"
```

**Oczekiwane:** Filtry ceny w `/api/produkty/szukaj-qdsl` działają tak jak w wersji Criteria, także razem z innymi filtrami.

**Pytanie dodatkowe:** Czym `BooleanBuilder` różni się od `BooleanExpression`? Do czego służy `ExpressionUtils.allOf(...)` i jak traktuje `null`?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
