#!/usr/bin/env bash
# Scenariusz 16-wszystkie-tagi: Filtr tagi=eko,promocja zwraca produkty z którymkolwiek tagiem
# Uruchom: ./scenarios/16-wszystkie-tagi/check.sh (z dowolnego katalogu). Kompiluje scenariusz i uruchamia jego testy.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 16-wszystkie-tagi: Filtr tagi=eko,promocja zwraca produkty z którymkolwiek tagiem"
run_tests "$SCEN_DIR"
summary
