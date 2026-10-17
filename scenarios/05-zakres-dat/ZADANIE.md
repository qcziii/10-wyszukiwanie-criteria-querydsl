# 05. Raport dodane do 31.03 nie zawiera produktów z 31.03

Poziom: **łatwy** · API: **Criteria**

## Uruchomienie

```bash
cd scenarios/05-zakres-dat
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/05-zakres-dat/check.sh
```

## Zgłoszenie

Raport „produkty dodane w marcu” (od 2026-03-01 do 2026-03-31) nie zawiera produktów dodanych 31 marca w ciągu dnia. Produkt dodany 31 marca o północy jest, a te z 12:00 i 23:59 nie.

```bash
curl "http://localhost:8080/api/produkty/szukaj?dodanyOd=2026-03-31&dodanyDo=2026-03-31"
```

**Oczekiwane:** `dodanyDo` obejmuje cały podany dzień. `dodanyOd=2026-03-31&dodanyDo=2026-03-31` zwraca trzy produkty z 31 marca (00:00, 12:00, 23:59), a produkt z 1 kwietnia 00:00 do marca nie należy.

**Pytanie dodatkowe:** Dlaczego `lessThanOrEqualTo(dodany, dodanyDo.atTime(23, 59, 59))` też nie jest dobrą poprawką?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
