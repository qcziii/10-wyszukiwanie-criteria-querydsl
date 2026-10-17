#!/usr/bin/env bash
# Scenariusz 01-pusty-parametr: Z pustym polem kategoria wyszukiwarka nic nie zwraca
# Uruchom: ./scenarios/01-pusty-parametr/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 01-pusty-parametr: Z pustym polem kategoria wyszukiwarka nic nie zwraca"
run_tests "$SCEN_DIR"
summary
