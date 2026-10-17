# 14. Parametr sort w wersji QueryDSL nic nie zmienia

Poziom: **średni** · API: **QueryDSL**

## Uruchomienie

```bash
cd scenarios/14-sortowanie-ignorowane
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/14-sortowanie-ignorowane/check.sh
```

## Zgłoszenie

W wersji QueryDSL parametr `sort` nic nie zmienia: `sort=cena,desc` daje produkty po kolei według id. Przy okazji: `sort=cenaZakupu` zwraca 200, a w wersji Criteria poprawnie 400 (po cenie zakupu nie wolno sortować).

```bash
curl "http://localhost:8080/api/produkty/szukaj-qdsl?sort=cena,desc"
curl -i "http://localhost:8080/api/produkty/szukaj-qdsl?sort=cenaZakupu"
```

**Oczekiwane:** Sortowanie w `/api/produkty/szukaj-qdsl` działa tak jak w wersji Criteria: te same pola (`nazwa`, `cena`, `dodany`, `kategoria`), kilka pól naraz, ta sama kolejność na kolejnych stronach. Inne pole to błąd 400.

**Pytanie dodatkowe:** Spring Data ma klasę `Querydsl` z metodą `applyPagination(pageable, query)`. Czy wystarczy jej użyć? Co z `sort=cenaZakupu`?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
