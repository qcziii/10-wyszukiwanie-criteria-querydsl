#!/usr/bin/env bash
# Scenariusz 10-niemutowalne-wyrazenie: Filtr ceny w wersji QueryDSL jest ignorowany
# Uruchom: ./scenarios/10-niemutowalne-wyrazenie/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 10-niemutowalne-wyrazenie: Filtr ceny w wersji QueryDSL jest ignorowany"
run_tests "$SCEN_DIR"
summary
