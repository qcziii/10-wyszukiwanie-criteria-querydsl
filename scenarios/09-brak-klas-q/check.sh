#!/usr/bin/env bash
# Scenariusz 09-brak-klas-q: Po dodaniu metamodelu Hibernate kompilacja nie widzi QProdukt
# Uruchom: ./scenarios/09-brak-klas-q/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 09-brak-klas-q: Po dodaniu metamodelu Hibernate kompilacja nie widzi QProdukt"
run_tests "$SCEN_DIR"
summary
