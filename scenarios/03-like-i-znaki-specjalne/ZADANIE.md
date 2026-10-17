# 03. Szukanie laptop nie znajduje Laptop Dell, a 100% znajduje za dużo

Poziom: **łatwy/średni (2 błędy)** · API: **Criteria**

## Uruchomienie

```bash
cd scenarios/03-like-i-znaki-specjalne
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/03-like-i-znaki-specjalne/check.sh
```

## Zgłoszenie

Szukanie „laptop” nie znajduje „Laptop Dell XPS 13”. Gdy wpiszę dokładną nazwę, znajduje. Klienci szukają też „100%” (bony rabatowe) i „kabel_usb”: po naszej pierwszej poprawce te wyszukiwania zaczęły zwracać za dużo.

```bash
curl "http://localhost:8080/api/produkty/szukaj?nazwa=laptop"
curl "http://localhost:8080/api/produkty/szukaj?nazwa=100%25"       # %25 to zakodowany znak %
curl "http://localhost:8080/api/produkty/szukaj?nazwa=kabel_usb"
```

**Oczekiwane:** `nazwa` to fragment nazwy bez względu na wielkość liter (też polskich: `ŁÓDKA` znajduje „łódka wędkarska”). Znaki `%` i `_` wpisane przez użytkownika oznaczają same siebie: `100%` znajduje tylko „Bon rabatowy 100%”, `kabel_usb` tylko „kabel_usb-c 1 m”.

**Pytanie dodatkowe:** Na PostgreSQL ten sam kod zachowuje się tak samo, a na MySQL z domyślnym porównywaniem napisów pierwszy błąd byłby niewidoczny. Dlaczego? Czym różni się `ILIKE` od `lower(...) like`?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
