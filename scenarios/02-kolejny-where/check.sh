#!/usr/bin/env bash
# Scenariusz 02-kolejny-where: Z dwoma filtrami działa tylko jeden
# Uruchom: ./scenarios/02-kolejny-where/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 02-kolejny-where: Z dwoma filtrami działa tylko jeden"
run_tests "$SCEN_DIR"
summary
