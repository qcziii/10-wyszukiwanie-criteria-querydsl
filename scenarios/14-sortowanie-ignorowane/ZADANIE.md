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

W wersji QueryDSL parametr `sort` nic nie zmienia: `sort=price,desc` daje produkty po kolei według id. Przy okazji: `sort=purchasePrice` zwraca 200, a w wersji Criteria poprawnie 400 (po cenie zakupu nie wolno sortować).

```bash
curl "http://localhost:8080/api/products/search-qdsl?sort=price,desc"
curl -i "http://localhost:8080/api/products/search-qdsl?sort=purchasePrice"
```

**Oczekiwane:** Sortowanie w `/api/products/search-qdsl` działa tak jak w wersji Criteria: te same pola (`name`, `price`, `addedAt`, `category`), kilka pól naraz, ta sama kolejność na kolejnych stronach. Inne pole to błąd 400.

**Pytanie dodatkowe:** Spring Data ma klasę `Querydsl` z metodą `applyPagination(pageable, query)`. Czy wystarczy jej użyć? Co z `sort=purchasePrice`?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
