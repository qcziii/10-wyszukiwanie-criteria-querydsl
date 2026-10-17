# 11. Produkty bez kategorii znikają z wyników QueryDSL

Poziom: **średni** · API: **QueryDSL**

## Uruchomienie

```bash
cd scenarios/11-znikaja-produkty-bez-kategorii
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/11-znikaja-produkty-bez-kategorii/check.sh
```

## Zgłoszenie

W wersji QueryDSL brakuje produktów bez kategorii (np. „Bon rabatowy 100%”, „Router Wi-Fi 6”). Do tego `totalElements` mówi 60, a na ostatniej stronie jest 5 produktów zamiast 10. W wersji Criteria wszystko się zgadza.

```bash
curl "http://localhost:8080/api/produkty/szukaj-qdsl?size=10&page=5"
```

**Oczekiwane:** Wersja QueryDSL zwraca wszystkie produkty, także bez kategorii, a liczba produktów na stronach zgadza się z `totalElements`. Filtr i sortowanie po kategorii nadal działają.

**Pytanie dodatkowe:** Co by się stało, gdyby w `orderBy` albo `where` użyć ścieżki `produkt.kategoria.nazwa` bez jawnego `leftJoin`? Czy Hibernate 7 doda wtedy złączenie zewnętrzne?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
