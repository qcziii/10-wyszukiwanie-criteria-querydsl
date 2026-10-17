#!/usr/bin/env bash
# Scenariusz 03-like-i-znaki-specjalne: Szukanie laptop nie znajduje Laptop Dell, a 100% znajduje za dużo
# Uruchom: ./scenarios/03-like-i-znaki-specjalne/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 03-like-i-znaki-specjalne: Szukanie laptop nie znajduje Laptop Dell, a 100% znajduje za dużo"
run_tests "$SCEN_DIR"
summary
