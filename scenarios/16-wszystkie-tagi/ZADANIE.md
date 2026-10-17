# 16. Filtr tagi=eko,promocja zwraca produkty z którymkolwiek tagiem

Poziom: **trudny** · API: **Criteria i QueryDSL**

## Uruchomienie

```bash
cd scenarios/16-wszystkie-tagi
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/16-wszystkie-tagi/check.sh
```

## Zgłoszenie

Filtr tagów ma zwracać produkty, które mają **wszystkie** wybrane tagi (tak jest opisane w `KryteriaWyszukiwania`). Tymczasem `tagi=eko,promocja` zwraca produkty z którymkolwiek z nich, w obu wersjach wyszukiwarki.

```bash
curl "http://localhost:8080/api/produkty/szukaj?tagi=eko,promocja"
curl "http://localhost:8080/api/produkty/szukaj-qdsl?tagi=eko,promocja"
```

**Oczekiwane:** W obu wersjach `tagi=eko,promocja` zwraca tylko produkty z oboma tagami, `tagi=EKO,promocja,nowosc` z trzema (bez względu na wielkość liter), a stronicowanie i `totalElements` są poprawne.

**Pytanie dodatkowe:** Jak zrobić „wszystkie tagi” jednym zapytaniem z `group by ... having count(distinct tag) = :n`? Kiedy to podejście jest lepsze od osobnych `EXISTS`?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
