# 06. Po filtrze po ocenie produkty się powtarzają

Poziom: **średni** · API: **Criteria**

## Uruchomienie

```bash
cd scenarios/06-powtorzone-produkty
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/06-powtorzone-produkty/check.sh
```

## Zgłoszenie

Filtr „tylko produkty z opinią co najmniej 4 gwiazdki” pokazuje dziwne rzeczy: `totalElements` jest większe niż liczba takich produktów, a pierwsza strona po 10 ma tylko 6 produktów, choć wyników jest kilkadziesiąt.

```bash
curl "http://localhost:8080/api/produkty/szukaj?ocenaMin=4&size=10"
```

**Oczekiwane:** Każdy produkt raz, `totalElements` równe liczbie produktów, które mają co najmniej jedną opinię z oceną `>= ocenaMin`, i pełne strony po 10.

**Pytanie dodatkowe:** Dlaczego na liście nie ma powtórzonych produktów, skoro SQL zwraca powtórzone wiersze? Co by się stało z tym samym kodem w Hibernate 5?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
