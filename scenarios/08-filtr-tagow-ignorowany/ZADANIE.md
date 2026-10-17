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

Filtr po tagu przestał działać: `tagi=outlet` zwraca wszystkie produkty. Za to `tagi=xyz` (nieistniejący tag) poprawnie zwraca pustą listę, więc filtr „coś robi”.

```bash
curl "http://localhost:8080/api/produkty/szukaj?tagi=outlet"
curl "http://localhost:8080/api/produkty/szukaj?tagi=xyz"
```

**Oczekiwane:** `tagi=outlet` zwraca produkty z tagiem outlet, `tagi=eko,promocja` produkty z oboma tagami, filtr łączy się z innymi (`tagi=eko&kategoria=dom`).

**Pytanie dodatkowe:** Czym różni się `subquery.correlate(produkt)` od `subquery.from(Produkt.class)`? Jak wygląda odpowiednik w QueryDSL?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
