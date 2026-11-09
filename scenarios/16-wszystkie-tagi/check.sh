#!/usr/bin/env bash
# Check for scenario 16-wszystkie-tagi (the description is printed below)
# Run: ./scenarios/16-wszystkie-tagi/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 16-wszystkie-tagi: Filtr tags=eko,promocja zwraca products z którymkolwiek tagiem"
run_tests "$SCEN_DIR"
summary
