#!/usr/bin/env bash
# Scenariusz 11-znikaja-produkty-bez-kategorii: Produkty bez kategorii znikają z wyników QueryDSL
# Uruchom: ./scenarios/11-znikaja-produkty-bez-kategorii/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 11-znikaja-produkty-bez-kategorii: Produkty bez kategorii znikają z wyników QueryDSL"
run_tests "$SCEN_DIR"
summary
