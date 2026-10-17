#!/usr/bin/env bash
# Scenariusz 08-filtr-tagow-ignorowany: Filtr po tagu zwraca wszystkie produkty
# Uruchom: ./scenarios/08-filtr-tagow-ignorowany/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 08-filtr-tagow-ignorowany: Filtr po tagu zwraca wszystkie produkty"
run_tests "$SCEN_DIR"
summary
