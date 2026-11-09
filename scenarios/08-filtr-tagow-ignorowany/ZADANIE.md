# 08. Filtr po tagu zwraca wszystkie produkty

Poziom: **średni** · API: **Criteria**

## Uruchomienie

```bash
cd scenarios/08-filtr-tagow-ignorowany
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/08-filtr-tagow-ignorowany/check.sh
```

## Zgłoszenie

Filtr po tagu przestał działać: `tags=outlet` zwraca wszystkie produkty. Za to `tags=xyz` (nieistniejący tag) poprawnie zwraca pustą listę, więc filtr „coś robi”.

```bash
curl "http://localhost:8080/api/products/search?tags=outlet"
curl "http://localhost:8080/api/products/search?tags=xyz"
```

**Oczekiwane:** `tags=outlet` zwraca produkty z tagiem outlet, `tags=eko,promocja` produkty z oboma tagami, filtr łączy się z innymi (`tags=eko&category=dom`).

**Pytanie dodatkowe:** Czym różni się `subquery.correlate(product)` od `subquery.from(Product.class)`? Jak wygląda odpowiednik w QueryDSL?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
