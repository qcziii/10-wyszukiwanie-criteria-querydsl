#!/usr/bin/env bash
# Scenariusz 15-n-plus-1: Jedno wyszukiwanie wysyła do bazy kilkanaście zapytań
# Uruchom: ./scenarios/15-n-plus-1/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 15-n-plus-1: Jedno wyszukiwanie wysyła do bazy kilkanaście zapytań"
run_tests "$SCEN_DIR"
summary
