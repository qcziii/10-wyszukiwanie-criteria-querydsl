# 02. Z dwoma filtrami działa tylko jeden

Poziom: **łatwy** · API: **Criteria**

## Uruchomienie

```bash
cd scenarios/02-kolejny-where
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/02-kolejny-where/check.sh
```

## Zgłoszenie

Każdy filtr osobno działa: „nazwa laptop” daje laptopy, „cena do 3000” daje tańsze produkty. Razem zwracają wszystko do 3000 zł, łącznie z czajnikiem. Wygląda, jakby liczył się tylko ostatni filtr.

```bash
curl "http://localhost:8080/api/produkty/szukaj?nazwa=laptop"
curl "http://localhost:8080/api/produkty/szukaj?nazwa=laptop&cenaDo=3000"
```

**Oczekiwane:** Filtry łączą się przez AND: `nazwa=laptop&cenaDo=3000` zwraca tylko laptopy do 3000 zł, `kategoria=telefony&cenaOd=500&cenaDo=1500` tylko telefony w tym zakresie.

**Pytanie dodatkowe:** Co mówi dokumentacja `CriteriaQuery.where(...)` o warunkach dodanych wcześniej? Jak wyglądałby ten sam błąd w QueryDSL (`query.where(a); query.where(b);`)?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
