#!/usr/bin/env bash
# Scenariusz 07-specification-i-stronicowanie: Po przejściu na Specification stronicowanie kończy się błędem
# Uruchom: ./scenarios/07-specification-i-stronicowanie/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 07-specification-i-stronicowanie: Po przejściu na Specification stronicowanie kończy się błędem"
run_tests "$SCEN_DIR"
summary
