#!/usr/bin/env bash
# Check for scenario 04-nazwy-atrybutow (the description is printed below)
# Run: ./scenarios/04-nazwy-atrybutow/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 04-nazwy-atrybutow: Po zmianie names pola wyszukiwarka zwraca 500"
run_tests "$SCEN_DIR"

if grep -qE '(get|join|fetch)\("' "$SCEN_DIR/src/main/java/pl/kurs/shop/search/CriteriaSearch.java"; then
  bad "CriteriaSearch nadal podaje names atrybutów w stringach, np. get(\"...\"); użyj metamodelu"
else
  ok "CriteriaSearch nie podaje nazw atrybutów w stringach"
fi
summary
