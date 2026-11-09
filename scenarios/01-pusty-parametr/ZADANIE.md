# 01. Z pustym polem kategoria wyszukiwarka nic nie zwraca

Poziom: **łatwy** · API: **Criteria**

## Uruchomienie

```bash
cd scenarios/01-pusty-parametr
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/01-pusty-parametr/check.sh
```

## Zgłoszenie

W formularzu wyszukiwania (i w kolekcji Postmana) pola, których użytkownik nie wypełnił, idą w adresie jako puste parametry. Od kiedy ktoś dodał do formularza listę kategorii, wyszukiwanie „wszystko do 100 zł” nic nie zwraca. Gdy usunę z adresu parametr `category`, działa.

```bash
curl "http://localhost:8080/api/products/search?name=&category=&priceTo=100"
curl "http://localhost:8080/api/products/search?priceTo=100"
```

**Oczekiwane:** Pusty parametr (albo same spacje) oznacza „nie filtruj po tym”, tak jak brak parametru. Filtr kategorii z wartością nadal działa (`category=ogród`).

**Pytanie dodatkowe:** Dlaczego pusty parametr `name=` nie psuł wyników, a pusty `category=` tak? Spójrz, jaki SQL powstaje w obu przypadkach.

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
