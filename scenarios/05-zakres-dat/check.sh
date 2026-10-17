#!/usr/bin/env bash
# Scenariusz 05-zakres-dat: Raport dodane do 31.03 nie zawiera produktów z 31.03
# Uruchom: ./scenarios/05-zakres-dat/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 05-zakres-dat: Raport dodane do 31.03 nie zawiera produktów z 31.03"
run_tests "$SCEN_DIR"
summary
