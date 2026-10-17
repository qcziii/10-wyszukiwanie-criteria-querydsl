# 09. Po dodaniu metamodelu Hibernate kompilacja nie widzi QProdukt

Poziom: **średni** · API: **QueryDSL (build)**

## Uruchomienie

```bash
cd scenarios/09-brak-klas-q
mvn spring-boot:run                 # aplikacja na http://localhost:8080, zatrzymanie: Ctrl+C

# automatyczne sprawdzenie (z katalogu głównego ćwiczeń); kompiluje i uruchamia testy, aplikacja nie musi działać
./scenarios/09-brak-klas-q/check.sh
```

## Zgłoszenie

Do wyszukiwarki Criteria dodaliśmy metamodel Hibernate (`Produkt_`), więc w `pom.xml` pojawił się `hibernate-processor` w `annotationProcessorPaths`. Od tej pory projekt się nie kompiluje: `cannot find symbol: class QProdukt`. Zależność `querydsl-apt` przecież jest.

```bash
mvn -q compile
```

**Oczekiwane:** Projekt się kompiluje, generują się i `Produkt_` (Criteria), i `QProdukt` (QueryDSL), obie wyszukiwarki działają.

**Pytanie dodatkowe:** Od JDK 23 `javac` domyślnie nie uruchamia procesorów znalezionych na classpath. Co to oznacza dla projektów, które polegały na samej zależności `querydsl-apt`?

## Kiedy skończysz

`check.sh` ma wypisać `WYNIK: wszystkie sprawdzenia przeszły`. Omówienie rozwiązania przedstawi prowadzący po zajęciach.
