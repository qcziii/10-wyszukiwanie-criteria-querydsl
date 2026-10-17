#!/usr/bin/env bash
# Scenariusz 04-nazwy-atrybutow: Po zmianie nazwy pola wyszukiwarka zwraca 500
# Uruchom: ./scenarios/04-nazwy-atrybutow/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 04-nazwy-atrybutow: Po zmianie nazwy pola wyszukiwarka zwraca 500"
run_tests "$SCEN_DIR"

if grep -qE '(get|join|fetch)\("' "$SCEN_DIR/src/main/java/pl/kurs/sklep/wyszukiwanie/WyszukiwarkaCriteria.java"; then
  bad "WyszukiwarkaCriteria nadal podaje nazwy atrybutów w stringach, np. get(\"...\"); użyj metamodelu"
else
  ok "WyszukiwarkaCriteria nie podaje nazw atrybutów w stringach"
fi
summary
