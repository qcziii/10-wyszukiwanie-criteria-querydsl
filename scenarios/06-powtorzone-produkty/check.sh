#!/usr/bin/env bash
# Scenariusz 06-powtorzone-produkty: Po filtrze po ocenie produkty się powtarzają
# Uruchom: ./scenarios/06-powtorzone-produkty/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 06-powtorzone-produkty: Po filtrze po ocenie produkty się powtarzają"
run_tests "$SCEN_DIR"
summary
