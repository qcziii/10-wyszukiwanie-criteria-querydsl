# 12. Filtr nie znajduje lapt, za to pozwala filtrować po cenie zakupu

Poziom: **średni** · API: **QueryDSL (web)**

## Uruchomienie

```bash
cd scenarios/12-querydsl-predicate-w-kontrolerze
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/12-querydsl-predicate-w-kontrolerze/check.sh
```

## Zgłoszenie

Panel administracyjny korzysta z prostego filtra `/api/produkty/filtr`, w którym Spring Data sam buduje warunek QueryDSL z parametrów (`@QuerydslPredicate`). Dwa zgłoszenia: wpisanie „lapt” nic nie znajduje (trzeba podać pełną nazwę z wielkimi literami), a audyt bezpieczeństwa zauważył, że `?cenaZakupu=...` filtruje po poufnej cenie zakupu, więc da się ją odgadnąć.

```bash
curl "http://localhost:8080/api/produkty/filtr?nazwa=lapt"
curl "http://localhost:8080/api/produkty/filtr?cenaZakupu=0.00"
```

**Oczekiwane:** Teksty (`nazwa`, `kategoria.nazwa`) szukają fragmentu bez względu na wielkość liter, `cena` dalej filtruje dokładnie. Filtrować można tylko po `nazwa`, `cena`, `dostepny`, `kategoria.nazwa`; inne parametry (np. `cenaZakupu`) są ignorowane.

**Pytanie dodatkowe:** Filtr przyjmuje też `Pageable`. Czy `sort=cenaZakupu` na `/api/produkty/filtr` jest bezpieczne? Co zrobić z sortowaniem?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
