# Wyszukiwanie po kryteriach: JPA Criteria API i QueryDSL, warsztat z troubleshootingu

Masz przed sobą 16 wersji wyszukiwarki produktów w sklepie, w każdej coś jest nie tak. Każda to osobna aplikacja Spring Boot z opisem zgłoszenia od użytkownika (`ZADANIE.md`) i testem (`check.sh`), który przejdzie dopiero po naprawie. Twoim zadaniem jest znaleźć przyczynę, poprawić kod i umieć wyjaśnić, **jak** do przyczyny doszedłeś.

Ta sama wyszukiwarka jest w każdej aplikacji napisana dwa razy: na **JPA Criteria API** i na **QueryDSL**. Obie wersje mają zwracać dokładnie te same wyniki, więc druga wersja to zawsze dobry punkt odniesienia.

## Wymagania

- Java 21 i Maven 3.9+
- `bash` (na Windows: Git Bash) do uruchamiania `check.sh`
- Postman albo `curl` do ręcznych zapytań
- wolny port 8080 (tylko gdy uruchamiasz aplikację ręcznie; `check.sh` portu nie potrzebuje)
- internet przy pierwszym uruchomieniu (Maven pobiera zależności)

Nie potrzebujesz Dockera ani bazy danych: aplikacje używają bazy H2 w pamięci, która przy każdym starcie ładuje te same dane (`src/main/resources/data.sql`, 60 produktów).

Stos: Spring Boot 4.1, Hibernate ORM 7.4, Spring Data JPA, QueryDSL 7.7 (fork OpenFeign), H2.

## Architektura

```
Postman / curl ──> localhost:8080 ──> [aplikacja sklep] ──> H2 w pamięci (produkt, kategoria, producent, tag, opinia)
```

| Endpoint | Opis |
|---|---|
| `GET /api/produkty/szukaj` | wyszukiwarka na JPA Criteria API (`WyszukiwarkaCriteria`) |
| `GET /api/produkty/szukaj-qdsl` | ta sama wyszukiwarka na QueryDSL (`WyszukiwarkaQuerydsl`) |
| `GET /api/produkty/filtr` | tylko w scenariuszu 12: filtr budowany przez Spring Data (`@QuerydslPredicate`) |
| `GET /api/produkty/lista` | tylko w scenariuszu 13: lekka lista produktów (projekcja QueryDSL) |

Parametry wyszukiwarki (wszystkie opcjonalne, opis w `KryteriaWyszukiwania.java`):

```
nazwa=laptop            fragment nazwy, bez względu na wielkość liter
kategoria=dom           nazwa kategorii, bez względu na wielkość liter
cenaOd=100&cenaDo=500   zakres cen, włącznie
dodanyOd=2026-03-01&dodanyDo=2026-03-31   zakres dat dodania, włącznie (cały ostatni dzień)
ocenaMin=4              produkt ma co najmniej jedną opinię z taką albo wyższą oceną
tagi=eko,promocja       produkt ma WSZYSTKIE podane tagi
page=0&size=20          stronicowanie
sort=cena,desc          sortowanie: nazwa, cena, dodany, kategoria (inne pole to błąd 400)
```

Przykład: `http://localhost:8080/api/produkty/szukaj?nazwa=laptop&cenaDo=3000&sort=cena,desc`

## Jak pracować

```bash
cd scenarios/01-pusty-parametr
mvn spring-boot:run               # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# w drugim terminalu albo w Postmanie
curl "http://localhost:8080/api/produkty/szukaj?cenaDo=100"

# automatyczne sprawdzenie, z katalogu głównego repo (aplikacja nie musi działać)
./scenarios/01-pusty-parametr/check.sh
```

Przeczytaj `ZADANIE.md` w katalogu scenariusza, odtwórz problem, znajdź przyczynę, popraw i sprawdź. Scenariusz jest rozwiązany, gdy `check.sh` wypisze `WYNIK: wszystkie sprawdzenia przeszły`.

`check.sh` kompiluje scenariusz i uruchamia jego testy (`mvn test`, klasa `CheckTest`). Testy wołają endpointy tak jak Postman i porównują wynik z tym, co wynika z danych w bazie. Część testów liczy też zapytania SQL, które aplikacja wysłała do bazy. Logi aplikacji z testów są w `target/surefire-reports/*-output.txt`.

Projekt otwierasz w IntelliJ jako projekt Maven (plik `pom.xml` w katalogu głównym otwiera wszystkie scenariusze naraz). Klasy `QProdukt`, `Produkt_` itd. są generowane przy kompilacji; jeśli IntelliJ ich nie widzi, uruchom `mvn compile` albo „Build > Rebuild Project”.

## Narzędzia

```yaml
# src/main/resources/application.yml scenariusza
logging.level.org.hibernate.SQL: debug                 # każde zapytanie SQL (włączone)
logging.level.org.hibernate.orm.jdbc.bind: trace       # wartości parametrów zapytań (dopisz, gdy potrzebujesz)
logging.level.org.hibernate.session.metrics: debug     # po każdym żądaniu: ile zapytań JDBC i ile trwały (włączone)
```

- **Konsola H2:** http://localhost:8080/h2-console, JDBC URL `jdbc:h2:mem:sklep`, użytkownik `sa`, bez hasła. Wklej tam SQL z logu i zobacz, co zwraca baza.
- **Wygenerowane klasy:** `target/generated-sources/annotations/pl/kurs/sklep/model` (metamodel JPA `Produkt_` i klasy QueryDSL `QProdukt`).
- **Debugger:** pułapka w `WyszukiwarkaCriteria.szukaj` albo `WyszukiwarkaQuerydsl.szukaj` i uruchomienie `SklepApplication` w trybie debug.
- **Porównanie wersji:** to samo zapytanie na `/szukaj` i `/szukaj-qdsl`. Jeśli wyniki się różnią, porównaj SQL z logu.
- **Ściąga:** [SCIAGA.md](SCIAGA.md), odpowiedniki Criteria i QueryDSL obok siebie.

## Port 8080 zajęty

Jeśli `mvn spring-boot:run` kończy się komunikatem `Port 8080 was already in use`, działa inna aplikacja (np. poprzedni scenariusz w innym terminalu). Zatrzymaj ją albo uruchom na innym porcie: `mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`. Na Windows w PowerShellu znajdziesz proces:

```powershell
Get-NetTCPConnection -LocalPort 8080 -State Listen | Select-Object LocalPort, OwningProcess, @{n='Name';e={(Get-Process -Id $_.OwningProcess).ProcessName}}
```

## Scenariusze

| # | Zgłoszenie | API | Poziom |
|---|---|---|---|
| 01 | Z pustym polem kategoria wyszukiwarka nic nie zwraca | Criteria | łatwy |
| 02 | Z dwoma filtrami działa tylko jeden | Criteria | łatwy |
| 03 | Szukanie laptop nie znajduje Laptop Dell, a 100% znajduje za dużo | Criteria | łatwy/średni (2 błędy) |
| 04 | Po zmianie nazwy pola wyszukiwarka zwraca 500 | Criteria | łatwy |
| 05 | Raport dodane do 31.03 nie zawiera produktów z 31.03 | Criteria | łatwy |
| 06 | Po filtrze po ocenie produkty się powtarzają | Criteria | średni |
| 07 | Po przejściu na Specification stronicowanie kończy się błędem | Criteria (Specification) | trudny |
| 08 | Filtr po tagu zwraca wszystkie produkty | Criteria | średni |
| 09 | Po dodaniu metamodelu Hibernate kompilacja nie widzi QProdukt | QueryDSL (build) | średni |
| 10 | Filtr ceny w wersji QueryDSL jest ignorowany | QueryDSL | łatwy |
| 11 | Produkty bez kategorii znikają z wyników QueryDSL | QueryDSL | średni |
| 12 | Filtr nie znajduje lapt, za to pozwala filtrować po cenie zakupu | QueryDSL (web) | średni |
| 13 | Na liście podpowiedzi nazwy produktów to nazwy kategorii | QueryDSL | średni |
| 14 | Parametr sort w wersji QueryDSL nic nie zmienia | QueryDSL | średni |
| 15 | Jedno wyszukiwanie wysyła do bazy kilkanaście zapytań | QueryDSL | średni |
| 16 | Filtr tagi=eko,promocja zwraca produkty z którymkolwiek tagiem | Criteria i QueryDSL | trudny |

## Zasady

- Naprawiasz kod aplikacji i `pom.xml` scenariusza. Nie zmieniaj testów (`src/test`), `check.sh` ani `scripts/`.
- Wymagania ze zgłoszenia są częścią zadania. Jeśli zgłoszenie mówi, że nie może wrócić N+1 albo błąd ma wychodzić przy kompilacji, rozwiązanie, które to łamie, nie jest rozwiązaniem.
- Zanim cokolwiek zmienisz, zobacz **jaki SQL** wysyła aplikacja. Większość błędów w dynamicznych zapytaniach widać od razu w SQL: brakujący warunek, zły rodzaj złączenia, brak limitu, zbyt wiele zapytań.
- Druga wersja wyszukiwarki (Criteria albo QueryDSL) jest poprawna, chyba że zgłoszenie mówi inaczej. Porównanie obu to najszybszy sposób, żeby zawęzić problem.
- Przy pracy w repo zrób własną gałąź, żeby łatwo wrócić do zepsutej wersji (`git checkout -- scenarios/NN-...`).

Wszystkie scenariusze kompiluje naraz `mvn compile` w katalogu głównym. Wszystkie testy naraz: `./scripts/check-all.sh` (na początku zajęć wszystkie są czerwone, i tak ma być).
