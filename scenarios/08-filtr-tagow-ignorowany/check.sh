#!/usr/bin/env bash
# Check for scenario 08-filtr-tagow-ignorowany (the description is printed below)
# Run: ./scenarios/08-filtr-tagow-ignorowany/check.sh (from any directory). Compiles the scenario and runs its tests.
SCEN_DIR=$(cd "$(dirname "$0")" && pwd)
source "$SCEN_DIR/../../scripts/lib.sh"
echo "Scenariusz 08-filtr-tagow-ignorowany: Filtr po tagu zwraca all products"
run_tests "$SCEN_DIR"
summary
