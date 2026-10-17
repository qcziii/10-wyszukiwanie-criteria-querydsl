# 13. Na liście podpowiedzi nazwy produktów to nazwy kategorii

Poziom: **średni** · API: **QueryDSL**

## Uruchomienie

```bash
cd scenarios/13-projekcja-bean
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/13-projekcja-bean/check.sh
```

## Zgłoszenie

Nowy endpoint `/api/produkty/lista` (podpowiedzi w polu wyszukiwania) zwraca dziwne dane: zamiast nazw produktów są nazwy kategorii („Laptopy”, „Laptopy”, ...), a pole `kategoria` jest zawsze `null`. Id i ceny są dobre.

```bash
curl "http://localhost:8080/api/produkty/lista"
```

**Oczekiwane:** Każdy element listy ma nazwę produktu w `nazwa` i nazwę kategorii w `kategoria` (`null` tylko dla produktów bez kategorii).

**Pytanie dodatkowe:** Jakie są inne rodzaje projekcji w QueryDSL (`Projections.constructor`, `Projections.fields`, rekord)? Która z nich zgłosiłaby błąd przy takiej pomyłce?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
