# 04. Po zmianie nazwy pola wyszukiwarka zwraca 500

Poziom: **łatwy** · API: **Criteria**

## Uruchomienie

```bash
cd scenarios/04-nazwy-atrybutow
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/04-nazwy-atrybutow/check.sh
```

## Zgłoszenie

Zmieniliśmy nazwę pola `cenaBrutto` na `cena` w encji `Produkt`. Projekt się kompiluje, aplikacja startuje, wyszukiwanie po nazwie działa. Ale filtr ceny i sortowanie po cenie kończą się błędem 500.

```bash
curl -i "http://localhost:8080/api/produkty/szukaj?cenaOd=1000"
curl -i "http://localhost:8080/api/produkty/szukaj?sort=cena,desc"
```

**Oczekiwane:** Filtr ceny i sortowanie po cenie działają. Dodatkowe wymaganie zespołu: następna zmiana nazwy pola ma zepsuć **kompilację**, a nie działanie aplikacji. `WyszukiwarkaCriteria` nie może więc podawać nazw atrybutów w stringach (`get("...")`, `join("...")`, `fetch("...")`); `check.sh` to sprawdza.

**Pytanie dodatkowe:** Skąd biorą się klasy `Produkt_`, `Kategoria_`? Gdzie je znaleźć po kompilacji i co się stanie, gdy ktoś usunie z `pom.xml` `hibernate-processor`?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
