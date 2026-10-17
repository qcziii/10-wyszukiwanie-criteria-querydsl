#!/usr/bin/env bash
# Scenariusz 14-sortowanie-ignorowane: Parametr sort w wersji QueryDSL nic nie zmienia
# Uruchom: ./scenarios/14-sortowanie-ignorowane/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 14-sortowanie-ignorowane: Parametr sort w wersji QueryDSL nic nie zmienia"
run_tests "$SCEN_DIR"
summary
