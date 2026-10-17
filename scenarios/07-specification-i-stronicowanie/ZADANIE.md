# 07. Po przejściu na Specification stronicowanie kończy się błędem

Poziom: **trudny** · API: **Criteria (Specification)**

## Uruchomienie

```bash
cd scenarios/07-specification-i-stronicowanie
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/07-specification-i-stronicowanie/check.sh
```

## Zgłoszenie

Przepisaliśmy wyszukiwarkę Criteria na `Specification`, żeby nie pisać ręcznie zapytania count. Lista „wszystko na jednej stronie” działa, ale gdy aplikacja mobilna prosi o strony po 5 produktów, dostaje błąd 500. Zależy nam na tym, żeby nie wrócił problem N+1 (kategoria i producent są pobierane od razu).

```bash
curl "http://localhost:8080/api/produkty/szukaj?size=100"    # działa
curl -i "http://localhost:8080/api/produkty/szukaj?size=5"    # 500
```

**Oczekiwane:** Stronicowanie działa (`size=5`, `page=1` z filtrem), `totalElements` jest poprawne, a jedna strona to najwyżej 3 zapytania SQL (strona, tagi, count), czyli bez N+1.

**Pytanie dodatkowe:** Dlaczego `size=100` działa? Kiedy Spring Data w ogóle nie wysyła zapytania count?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
